import React, { useState, useEffect } from 'react';
import {
  AppBar,
  Box,
  Container,
  CssBaseline,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
  Button,
  Card,
  CardContent,
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Menu as MenuIcon,
  Dashboard as DashboardIcon,
  EventNote as EventNoteIcon,
  History as HistoryIcon,
  AccountBalance as AccountBalanceIcon,
  Person as PersonIcon,
  Logout as LogoutIcon,
} from '@mui/icons-material';
import { useAuth } from '../../contexts/AuthContext';
import { leaveRequestService } from '../../services/leaveRequestService';
import { leaveBalanceService } from '../../services/leaveBalanceService';
import { leaveTypeService } from '../../services/leaveTypeService';
import { authService } from '../../services/authService';

const drawerWidth = 240;

const EmployeeDashboard = () => {
  const { user, logout } = useAuth();
  const [mobileOpen, setMobileOpen] = useState(false);
  const [currentView, setCurrentView] = useState('dashboard');
  const [leaveBalances, setLeaveBalances] = useState([]);
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [leaveTypes, setLeaveTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [applyDialogOpen, setApplyDialogOpen] = useState(false);
  const [applyForm, setApplyForm] = useState({
    leaveTypeId: '',
    startDate: '',
    endDate: '',
    reason: '',
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [balancesRes, requestsRes, typesRes] = await Promise.all([
        leaveBalanceService.getUserLeaveBalances(user.id),
        leaveRequestService.getMyLeaveRequests(),
        leaveTypeService.getAllLeaveTypes(),
      ]);
      setLeaveBalances(balancesRes.data || []);
      setLeaveRequests(requestsRes.data || []);
      setLeaveTypes(typesRes.data || []);
    } catch (err) {
      setError('Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const handleApplyLeave = async () => {
    try {
      await leaveRequestService.applyForLeave(applyForm);
      setApplyDialogOpen(false);
      setApplyForm({ leaveTypeId: '', startDate: '', endDate: '', reason: '' });
      loadData();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to apply for leave');
    }
  };

  const handleCancelLeave = async (id) => {
    try {
      await leaveRequestService.cancelLeave(id);
      loadData();
    } catch (err) {
      setError('Failed to cancel leave request');
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'APPROVED': return 'success';
      case 'REJECTED': return 'error';
      case 'PENDING': return 'warning';
      case 'CANCELLED': return 'default';
      default: return 'default';
    }
  };

  const drawer = (
    <div>
      <Toolbar>
        <Typography variant="h6" noWrap component="div">
          ELMS
        </Typography>
      </Toolbar>
      <List>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('dashboard')} selected={currentView === 'dashboard'}>
            <ListItemIcon><DashboardIcon /></ListItemIcon>
            <ListItemText primary="Dashboard" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('apply')} selected={currentView === 'apply'}>
            <ListItemIcon><EventNoteIcon /></ListItemIcon>
            <ListItemText primary="Apply Leave" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('history')} selected={currentView === 'history'}>
            <ListItemIcon><HistoryIcon /></ListItemIcon>
            <ListItemText primary="Leave History" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('balance')} selected={currentView === 'balance'}>
            <ListItemIcon><AccountBalanceIcon /></ListItemIcon>
            <ListItemText primary="Leave Balance" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('profile')} selected={currentView === 'profile'}>
            <ListItemIcon><PersonIcon /></ListItemIcon>
            <ListItemText primary="Profile" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={logout}>
            <ListItemIcon><LogoutIcon /></ListItemIcon>
            <ListItemText primary="Logout" />
          </ListItemButton>
        </ListItem>
      </List>
    </div>
  );

  const renderDashboard = () => (
    <Box>
      <Typography variant="h4" gutterBottom>
        Welcome, {user?.fullName}
      </Typography>
      <Grid container spacing={3} sx={{ mt: 2 }}>
        {leaveBalances.map((balance) => (
          <Grid item xs={12} sm={6} md={4} key={balance.id}>
            <Card>
              <CardContent>
                <Typography variant="h6" color="primary">
                  {balance.leaveTypeName}
                </Typography>
                <Typography variant="h3" sx={{ mt: 2 }}>
                  {balance.remainingDays}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Days Remaining
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      <Typography variant="h5" sx={{ mt: 4, mb: 2 }}>
        Recent Leave Requests
      </Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Leave Type</TableCell>
              <TableCell>Start Date</TableCell>
              <TableCell>End Date</TableCell>
              <TableCell>Days</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {leaveRequests.slice(0, 5).map((request) => (
              <TableRow key={request.id}>
                <TableCell>{request.leaveTypeName}</TableCell>
                <TableCell>{request.startDate}</TableCell>
                <TableCell>{request.endDate}</TableCell>
                <TableCell>{request.numberOfDays}</TableCell>
                <TableCell>
                  <Chip label={request.status} color={getStatusColor(request.status)} size="small" />
                </TableCell>
                <TableCell>
                  {request.status === 'PENDING' && (
                    <Button
                      size="small"
                      color="error"
                      onClick={() => handleCancelLeave(request.id)}
                    >
                      Cancel
                    </Button>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );

  const renderApplyLeave = () => (
    <Box>
      <Typography variant="h4" gutterBottom>
        Apply for Leave
      </Typography>
      <Paper sx={{ p: 3, mt: 2 }}>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <TextField
              select
              fullWidth
              label="Leave Type"
              value={applyForm.leaveTypeId}
              onChange={(e) => setApplyForm({ ...applyForm, leaveTypeId: e.target.value })}
            >
              {leaveTypes.map((type) => (
                <MenuItem key={type.id} value={type.id}>
                  {type.leaveName} (Max: {type.maximumDays} days)
                </MenuItem>
              ))}
            </TextField>
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Start Date"
              type="date"
              InputLabelProps={{ shrink: true }}
              value={applyForm.startDate}
              onChange={(e) => setApplyForm({ ...applyForm, startDate: e.target.value })}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="End Date"
              type="date"
              InputLabelProps={{ shrink: true }}
              value={applyForm.endDate}
              onChange={(e) => setApplyForm({ ...applyForm, endDate: e.target.value })}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              label="Reason"
              multiline
              rows={4}
              value={applyForm.reason}
              onChange={(e) => setApplyForm({ ...applyForm, reason: e.target.value })}
            />
          </Grid>
          <Grid item xs={12}>
            <Button variant="contained" onClick={handleApplyLeave}>
              Submit Leave Request
            </Button>
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );

  const renderLeaveHistory = () => (
    <Box>
      <Typography variant="h4" gutterBottom>
        Leave History
      </Typography>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Leave Type</TableCell>
              <TableCell>Start Date</TableCell>
              <TableCell>End Date</TableCell>
              <TableCell>Days</TableCell>
              <TableCell>Reason</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Applied Date</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {leaveRequests.map((request) => (
              <TableRow key={request.id}>
                <TableCell>{request.leaveTypeName}</TableCell>
                <TableCell>{request.startDate}</TableCell>
                <TableCell>{request.endDate}</TableCell>
                <TableCell>{request.numberOfDays}</TableCell>
                <TableCell>{request.reason}</TableCell>
                <TableCell>
                  <Chip label={request.status} color={getStatusColor(request.status)} size="small" />
                </TableCell>
                <TableCell>{new Date(request.appliedDate).toLocaleDateString()}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );

  const renderLeaveBalance = () => (
    <Box>
      <Typography variant="h4" gutterBottom>
        Leave Balance
      </Typography>
      <Grid container spacing={3} sx={{ mt: 2 }}>
        {leaveBalances.map((balance) => (
          <Grid item xs={12} sm={6} md={4} key={balance.id}>
            <Card>
              <CardContent>
                <Typography variant="h6" color="primary">
                  {balance.leaveTypeName}
                </Typography>
                <Typography variant="h3" sx={{ mt: 2 }}>
                  {balance.remainingDays}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Days Remaining
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );

  const renderProfile = () => (
    <Box>
      <Typography variant="h4" gutterBottom>
        Profile
      </Typography>
      <Paper sx={{ p: 3, mt: 2 }}>
        <Grid container spacing={3}>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Employee Code"
              value={user?.employeeCode}
              disabled
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Full Name"
              value={user?.fullName}
              disabled
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Email"
              value={user?.email}
              disabled
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Phone"
              value={user?.phone || 'N/A'}
              disabled
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Department"
              value={user?.department || 'N/A'}
              disabled
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Designation"
              value={user?.designation || 'N/A'}
              disabled
            />
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBar
        position="fixed"
        sx={{
          width: { sm: `calc(100% - ${drawerWidth}px)` },
          ml: { sm: `${drawerWidth}px` },
        }}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            edge="start"
            onClick={() => setMobileOpen(!mobileOpen)}
            sx={{ mr: 2, display: { sm: 'none' } }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap component="div">
            Employee Dashboard
          </Typography>
        </Toolbar>
      </AppBar>
      <Box
        component="nav"
        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
      >
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onClose={() => setMobileOpen(false)}
          ModalProps={{ keepMounted: true }}
          sx={{
            display: { xs: 'block', sm: 'none' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
          }}
        >
          {drawer}
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: 'none', sm: 'block' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
          }}
          open
        >
          {drawer}
        </Drawer>
      </Box>
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          width: { sm: `calc(100% - ${drawerWidth}px)` },
        }}
      >
        <Toolbar />
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        {currentView === 'dashboard' && renderDashboard()}
        {currentView === 'apply' && renderApplyLeave()}
        {currentView === 'history' && renderLeaveHistory()}
        {currentView === 'balance' && renderLeaveBalance()}
        {currentView === 'profile' && renderProfile()}
      </Box>
    </Box>
  );
};

export default EmployeeDashboard;
