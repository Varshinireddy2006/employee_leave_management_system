import React, { useState, useEffect } from 'react';
import {
  AppBar,
  Box,
  Toolbar,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Typography,
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
  Button,
  CircularProgress,
  Alert,
  Drawer,
  Tabs,
  Tab,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from '@mui/material';
import {
  Menu as MenuIcon,
  Dashboard as DashboardIcon,
  People as PeopleIcon,
  EventNote as EventNoteIcon,
  Category as CategoryIcon,
  Assessment as AssessmentIcon,
  Person as PersonIcon,
  Logout as LogoutIcon,
  Download as DownloadIcon,
} from '@mui/icons-material';
import { useAuth } from '../../contexts/AuthContext';
import { userService } from '../../services/userService';
import { leaveRequestService } from '../../services/leaveRequestService';
import { leaveTypeService } from '../../services/leaveTypeService';
import { reportService } from '../../services/reportService';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, PieChart, Pie, Cell } from 'recharts';

const drawerWidth = 240;

const AdminDashboard = () => {
  const { user, logout } = useAuth();
  const [mobileOpen, setMobileOpen] = useState(false);
  const [currentView, setCurrentView] = useState('dashboard');
  const [tabValue, setTabValue] = useState(0);
  const [users, setUsers] = useState([]);
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [leaveTypes, setLeaveTypes] = useState([]);
  const [reports, setReports] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogType, setDialogType] = useState('');
  const [selectedItem, setSelectedItem] = useState(null);
  const [formData, setFormData] = useState({});

  useEffect(() => {
    loadData();
  }, [currentView]);

  const loadData = async () => {
    try {
      setLoading(true);
      if (currentView === 'dashboard') {
        const reportsData = await reportService.getLeaveSummary();
        setReports(reportsData.data);
      } else if (currentView === 'users') {
        const usersData = await userService.getAllUsers();
        setUsers(usersData.data || []);
      } else if (currentView === 'leaves') {
        const requestsData = await leaveRequestService.getAllLeaveRequests();
        setLeaveRequests(requestsData.data || []);
      } else if (currentView === 'leaveTypes') {
        const typesData = await leaveTypeService.getAllLeaveTypes();
        setLeaveTypes(typesData.data || []);
      }
    } catch (err) {
      setError('Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async () => {
    try {
      await userService.createUser(formData);
      setDialogOpen(false);
      setFormData({});
      loadData();
    } catch (err) {
      setError('Failed to create user');
    }
  };

  const handleCreateLeaveType = async () => {
    try {
      await leaveTypeService.createLeaveType(formData);
      setDialogOpen(false);
      setFormData({});
      loadData();
    } catch (err) {
      setError('Failed to create leave type');
    }
  };

  const handleDeleteUser = async (id) => {
    try {
      await userService.deleteUser(id);
      loadData();
    } catch (err) {
      setError('Failed to delete user');
    }
  };

  const handleDeleteLeaveType = async (id) => {
    try {
      await leaveTypeService.deleteLeaveType(id);
      loadData();
    } catch (err) {
      setError('Failed to delete leave type');
    }
  };

  const handleExportCsv = async () => {
    try {
      const csvData = await reportService.exportToCsv();
      const blob = new Blob([csvData], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'leave_requests.csv';
      a.click();
    } catch (err) {
      setError('Failed to export CSV');
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
          ELMS Admin
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
          <ListItemButton onClick={() => setCurrentView('users')} selected={currentView === 'users'}>
            <ListItemIcon><PeopleIcon /></ListItemIcon>
            <ListItemText primary="User Management" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('leaves')} selected={currentView === 'leaves'}>
            <ListItemIcon><EventNoteIcon /></ListItemIcon>
            <ListItemText primary="Leave Requests" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('leaveTypes')} selected={currentView === 'leaveTypes'}>
            <ListItemIcon><CategoryIcon /></ListItemIcon>
            <ListItemText primary="Leave Types" />
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton onClick={() => setCurrentView('reports')} selected={currentView === 'reports'}>
            <ListItemIcon><AssessmentIcon /></ListItemIcon>
            <ListItemText primary="Reports" />
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

  const renderDashboard = () => {
    const employeeCount = users.filter(u => u.role === 'EMPLOYEE').length;
    const managerCount = users.filter(u => u.role === 'MANAGER').length;
    const pendingCount = leaveRequests.filter(r => r.status === 'PENDING').length;
    const approvedCount = leaveRequests.filter(r => r.status === 'APPROVED').length;
    const rejectedCount = leaveRequests.filter(r => r.status === 'REJECTED').length;

    const statusData = [
      { name: 'Pending', value: pendingCount, color: '#ff9800' },
      { name: 'Approved', value: approvedCount, color: '#4caf50' },
      { name: 'Rejected', value: rejectedCount, color: '#f44336' },
    ];

    const departmentData = users.reduce((acc, user) => {
      if (user.department) {
        acc[user.department] = (acc[user.department] || 0) + 1;
      }
      return acc;
    }, {});

    const departmentChartData = Object.entries(departmentData).map(([name, value]) => ({ name, value }));

    return (
      <Box>
        <Typography variant="h4" gutterBottom>
          Admin Dashboard
        </Typography>
        <Grid container spacing={3} sx={{ mt: 2 }}>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography variant="h6" color="primary">
                  Total Employees
                </Typography>
                <Typography variant="h3" sx={{ mt: 2 }}>
                  {employeeCount}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography variant="h6" color="secondary">
                  Total Managers
                </Typography>
                <Typography variant="h3" sx={{ mt: 2 }}>
                  {managerCount}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography variant="h6" color="warning.main">
                  Pending Leaves
                </Typography>
                <Typography variant="h3" sx={{ mt: 2 }}>
                  {pendingCount}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography variant="h6" color="success.main">
                  Approved Leaves
                </Typography>
                <Typography variant="h3" sx={{ mt: 2 }}>
                  {approvedCount}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>

        <Grid container spacing={3} sx={{ mt: 4 }}>
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>
                Leave Status Distribution
              </Typography>
              <PieChart width={400} height={300}>
                <Pie
                  data={statusData}
                  cx={200}
                  cy={150}
                  labelLine={false}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {statusData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </Paper>
          </Grid>
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>
                Department Distribution
              </Typography>
              <BarChart width={400} height={300} data={departmentChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="value" fill="#1976d2" />
              </BarChart>
            </Paper>
          </Grid>
        </Grid>
      </Box>
    );
  };

  const renderUsers = () => (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">User Management</Typography>
        <Button
          variant="contained"
          onClick={() => {
            setDialogType('user');
            setDialogOpen(true);
            setFormData({ role: 'EMPLOYEE' });
          }}
        >
          Add User
        </Button>
      </Box>
      <Tabs value={tabValue} onChange={(e, v) => setTabValue(v)} sx={{ mb: 2 }}>
        <Tab label="All Users" />
        <Tab label="Employees" />
        <Tab label="Managers" />
        <Tab label="Admins" />
      </Tabs>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Employee Code</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Department</TableCell>
              <TableCell>Designation</TableCell>
              <TableCell>Role</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users
              .filter(u => {
                if (tabValue === 0) return true;
                if (tabValue === 1) return u.role === 'EMPLOYEE';
                if (tabValue === 2) return u.role === 'MANAGER';
                if (tabValue === 3) return u.role === 'ADMIN';
                return true;
              })
              .map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.employeeCode}</TableCell>
                <TableCell>{user.fullName}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{user.department || 'N/A'}</TableCell>
                <TableCell>{user.designation || 'N/A'}</TableCell>
                <TableCell>
                  <Chip label={user.role} color={user.role === 'ADMIN' ? 'secondary' : 'default'} size="small" />
                </TableCell>
                <TableCell>
                  <Button size="small" color="error" onClick={() => handleDeleteUser(user.id)}>
                    Delete
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );

  const renderLeaveRequests = () => (
    <Box>
      <Typography variant="h4" gutterBottom>
        All Leave Requests
      </Typography>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Employee</TableCell>
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
                <TableCell>{request.employeeName}</TableCell>
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

  const renderLeaveTypes = () => (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">Leave Type Management</Typography>
        <Button
          variant="contained"
          onClick={() => {
            setDialogType('leaveType');
            setDialogOpen(true);
            setFormData({});
          }}
        >
          Add Leave Type
        </Button>
      </Box>
      <Grid container spacing={3}>
        {leaveTypes.map((type) => (
          <Grid item xs={12} sm={6} md={4} key={type.id}>
            <Card>
              <CardContent>
                <Typography variant="h6">{type.leaveName}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Maximum Days: {type.maximumDays}
                </Typography>
                <Button
                  size="small"
                  color="error"
                  sx={{ mt: 2 }}
                  onClick={() => handleDeleteLeaveType(type.id)}
                >
                  Delete
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );

  const renderReports = () => (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4">Reports</Typography>
        <Button
          variant="contained"
          startIcon={<DownloadIcon />}
          onClick={handleExportCsv}
        >
          Export CSV
        </Button>
      </Box>
      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Leave Summary
              </Typography>
              {reports && (
                <Box>
                  <Typography>Total Requests: {reports.totalRequests}</Typography>
                  <Typography>Pending: {reports.pending}</Typography>
                  <Typography>Approved: {reports.approved}</Typography>
                  <Typography>Rejected: {reports.rejected}</Typography>
                </Box>
              )}
            </CardContent>
          </Card>
        </Grid>
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
            Admin Dashboard
          </Typography>
        </Toolbar>
      </AppBar>
      <Box component="nav" sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}>
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
        {currentView === 'users' && renderUsers()}
        {currentView === 'leaves' && renderLeaveRequests()}
        {currentView === 'leaveTypes' && renderLeaveTypes()}
        {currentView === 'reports' && renderReports()}
        {currentView === 'profile' && renderProfile()}
      </Box>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>
          {dialogType === 'user' ? 'Add New User' : 'Add Leave Type'}
        </DialogTitle>
        <DialogContent>
          {dialogType === 'user' ? (
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Employee Code"
                  value={formData.employeeCode || ''}
                  onChange={(e) => setFormData({ ...formData, employeeCode: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Full Name"
                  value={formData.fullName || ''}
                  onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Email"
                  type="email"
                  value={formData.email || ''}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Password"
                  type="password"
                  value={formData.password || ''}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Phone"
                  value={formData.phone || ''}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Department"
                  value={formData.department || ''}
                  onChange={(e) => setFormData({ ...formData, department: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Designation"
                  value={formData.designation || ''}
                  onChange={(e) => setFormData({ ...formData, designation: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <FormControl fullWidth>
                  <InputLabel>Role</InputLabel>
                  <Select
                    value={formData.role || 'EMPLOYEE'}
                    onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                  >
                    <MenuItem value="EMPLOYEE">Employee</MenuItem>
                    <MenuItem value="MANAGER">Manager</MenuItem>
                    <MenuItem value="ADMIN">Admin</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
          ) : (
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Leave Name"
                  value={formData.leaveName || ''}
                  onChange={(e) => setFormData({ ...formData, leaveName: e.target.value })}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Maximum Days"
                  type="number"
                  value={formData.maximumDays || ''}
                  onChange={(e) => setFormData({ ...formData, maximumDays: parseInt(e.target.value) })}
                />
              </Grid>
            </Grid>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button
            onClick={dialogType === 'user' ? handleCreateUser : handleCreateLeaveType}
            variant="contained"
          >
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AdminDashboard;
