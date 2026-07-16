import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  CircularProgress,
  Grid,
  Card,
  CardContent,
  CardActionArea,
  Divider,
  Collapse,
} from '@mui/material';
import {
  AdminPanelSettings as AdminIcon,
  SupervisorAccount as ManagerIcon,
  Person as EmployeeIcon,
  ArrowForward as ArrowForwardIcon,
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showManualLogin, setShowManualLogin] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const from = location.state?.from?.pathname || '/';

  const handleRoleCardClick = (roleEmail, rolePassword) => {
    setEmail(roleEmail);
    setPassword(rolePassword);
    setShowManualLogin(true);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await login(email, password);
      navigate(from, { replace: true });
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const roles = [
    {
      title: 'Admin',
      description: 'Manage users, leave types, system settings and view reports.',
      icon: <AdminIcon sx={{ fontSize: 48, color: '#ffffff', filter: 'drop-shadow(0 0 4px rgba(255,255,255,0.5))' }} />,
      email: 'admin@elms.com',
      password: 'admin123',
      color: '#3B82F6',
      gradient: 'linear-gradient(45deg, #3B82F6 0%, #60A5FA 100%)',
      iconGradient: 'linear-gradient(135deg, rgba(37, 99, 235, 0.8) 0%, rgba(96, 165, 250, 0.4) 100%)',
      iconShadow: '0 0 25px rgba(37, 99, 235, 0.6), inset 0 0 12px rgba(255, 255, 255, 0.3)',
      iconBorder: '1px solid rgba(255, 255, 255, 0.4)',
    },
    {
      title: 'Manager',
      description: 'Review and approve/reject team leave requests.',
      icon: <ManagerIcon sx={{ fontSize: 48, color: '#ffffff', filter: 'drop-shadow(0 0 4px rgba(255,255,255,0.5))' }} />,
      email: 'manager1@elms.com',
      password: 'manager123',
      color: '#8B5CF6',
      gradient: 'linear-gradient(45deg, #8B5CF6 0%, #A78BFA 100%)',
      iconGradient: 'linear-gradient(135deg, rgba(139, 92, 246, 0.8) 0%, rgba(167, 139, 250, 0.4) 100%)',
      iconShadow: '0 0 25px rgba(139, 92, 246, 0.6), inset 0 0 12px rgba(255, 255, 255, 0.3)',
      iconBorder: '1px solid rgba(255, 255, 255, 0.4)',
    },
    {
      title: 'Employee',
      description: 'Apply for leaves, view balances, and check history.',
      icon: <EmployeeIcon sx={{ fontSize: 48, color: '#ffffff', filter: 'drop-shadow(0 0 4px rgba(255,255,255,0.5))' }} />,
      email: 'alice@elms.com',
      password: 'employee123',
      color: '#EC4899',
      gradient: 'linear-gradient(45deg, #EC4899 0%, #F472B6 100%)',
      iconGradient: 'linear-gradient(135deg, rgba(34, 197, 94, 0.8) 0%, rgba(74, 222, 128, 0.4) 100%)',
      iconShadow: '0 0 25px rgba(34, 197, 94, 0.6), inset 0 0 12px rgba(255, 255, 255, 0.3)',
      iconBorder: '1px solid rgba(255, 255, 255, 0.4)',
    },
  ];

  return (
    <Container component="main" maxWidth="md" sx={{ py: 6 }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        {/* Header */}
        <Typography
          component="h1"
          variant="h3"
          align="center"
          gutterBottom
          sx={{
            fontWeight: 'bold',
            background: 'linear-gradient(45deg, #3B82F6 0%, #EC4899 100%)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
            textShadow: '0 0 30px rgba(236, 72, 153, 0.3)',
            mb: 2,
            fontSize: { xs: '2.5rem', sm: '3.25rem', md: '3.75rem' },
          }}
        >
          Employee Leave Management System
        </Typography>
        <Typography variant="h6" align="center" sx={{ mb: 6, color: 'rgba(255, 255, 255, 0.8)', fontSize: '1.125rem' }}>
          Select a role to pre-fill credentials, or enter your details manually below
        </Typography>

        {error && (
          <Alert severity="error" sx={{ width: '100%', maxWidth: 'sm', mb: 3 }}>
            {error}
          </Alert>
        )}

        {loading && (
          <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', my: 4 }}>
            <CircularProgress size={50} sx={{ mb: 2 }} />
            <Typography variant="body1" color="text.secondary">
              Authenticating... Please wait
            </Typography>
          </Box>
        )}

        {!loading && (
          <>
            {/* Quick Login Roles Cards */}
            <Grid container spacing={6} sx={{ mb: 6, justifyContent: 'center' }}>
              {roles.map((role) => (
                <Grid item xs={12} sm={4} key={role.title}>
                  <Card
                    sx={{
                      height: '100%',
                      display: 'flex',
                      flexDirection: 'column',
                      borderRadius: 4,
                      border: `1px solid ${role.color}40`,
                      backgroundColor: 'rgba(15, 23, 42, 0.15)',
                      backdropFilter: 'blur(24px)',
                      boxShadow: `0 12px 40px rgba(0,0,0,0.4), 0 0 20px ${role.color}20`,
                      transition: 'all 0.3s ease-in-out',
                      '&:hover': {
                        transform: 'translateY(-8px)',
                        boxShadow: `0 16px 50px rgba(0,0,0,0.5), 0 0 30px ${role.color}40`,
                        border: `1px solid ${role.color}80`,
                      },
                    }}
                  >
                    <CardActionArea
                      onClick={() => handleRoleCardClick(role.email, role.password)}
                      sx={{
                        flexGrow: 1,
                        p: 4,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        textAlign: 'center',
                        '&:hover .role-btn': {
                          filter: 'brightness(1.15)',
                          boxShadow: `0 0 20px ${role.color}80`,
                        }
                      }}
                    >
                      <Box sx={{ 
                        width: 80, 
                        height: 80, 
                        borderRadius: '50%', 
                        background: role.iconGradient,
                        backdropFilter: 'blur(10px)',
                        border: role.iconBorder,
                        mb: 3, 
                        display: 'flex', 
                        justifyContent: 'center', 
                        alignItems: 'center',
                        boxShadow: role.iconShadow
                      }}>
                        {role.icon}
                      </Box>
                      <Typography variant="h5" component="div" sx={{ fontWeight: 'bold', fontSize: '28px', mb: 2, color: role.color }}>
                        {role.title}
                      </Typography>
                      <Typography variant="body1" sx={{ mb: 5, px: 1, color: '#FFFFFF', opacity: 0.95, fontSize: '15px', lineHeight: 1.6 }}>
                        {role.description}
                      </Typography>
                      <Box 
                        className="role-btn"
                        sx={{ 
                          mt: 'auto', 
                          width: '100%',
                          background: role.gradient,
                          color: '#fff',
                          fontWeight: 600,
                          fontSize: '16px',
                          borderRadius: '16px',
                          py: 1.5,
                          display: 'flex',
                          justifyContent: 'center',
                          alignItems: 'center',
                          boxShadow: `0 8px 20px ${role.color}40`,
                          transition: 'all 0.3s ease',
                        }}
                      >
                        Select Role <ArrowForwardIcon sx={{ ml: 1, fontSize: 22 }} />
                      </Box>
                    </CardActionArea>
                  </Card>
                </Grid>
              ))}
            </Grid>

            {/* Manual Sign-in Option */}
            <Box sx={{ width: '100%', maxWidth: 'sm', textAlign: 'center' }}>
              <Button
                variant="outlined"
                onClick={() => setShowManualLogin(!showManualLogin)}
                sx={{
                  borderRadius: 20,
                  px: 4,
                  py: 1,
                  textTransform: 'none',
                  fontWeight: 'bold',
                  mb: 3,
                }}
              >
                {showManualLogin ? 'Hide Credentials Form' : 'Or Login with Custom Email'}
              </Button>

              <Collapse in={showManualLogin || email !== ''}>
                <Paper elevation={3} sx={{ p: 4, borderRadius: 4, textAlign: 'left', mb: 4 }}>
                  <Typography variant="h5" sx={{ fontWeight: 'bold', mb: 3 }}>
                    Sign In
                  </Typography>
                  <Box component="form" onSubmit={handleSubmit}>
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      id="email"
                      label="Email Address"
                      name="email"
                      autoComplete="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      sx={{ mb: 2 }}
                    />
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      name="password"
                      label="Password"
                      type="password"
                      id="password"
                      autoComplete="current-password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      sx={{ mb: 3 }}
                    />
                    <Button
                      type="submit"
                      fullWidth
                      variant="contained"
                      size="large"
                      sx={{
                        borderRadius: 3,
                        py: 1.5,
                        fontWeight: 'bold',
                        textTransform: 'none',
                        mb: 2,
                      }}
                    >
                      Sign In
                    </Button>
                    <Box sx={{ textAlign: 'center', mt: 1 }}>
                      <Typography variant="body2" color="text.secondary">
                        Don't have an account?{' '}
                        <Button
                          color="primary"
                          onClick={() => navigate('/register')}
                          sx={{ textTransform: 'none', fontWeight: 'bold', p: 0, minWidth: 'auto' }}
                        >
                          Register Here
                        </Button>
                      </Typography>
                    </Box>
                  </Box>
                </Paper>
              </Collapse>
            </Box>
          </>
        )}
      </Box>
    </Container>
  );
};

export default LoginPage;
