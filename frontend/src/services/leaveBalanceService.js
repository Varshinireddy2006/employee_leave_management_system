import api from './api';

export const leaveBalanceService = {
  getAllLeaveBalances: async () => {
    const response = await api.get('/leave-balances');
    return response.data;
  },

  getUserLeaveBalances: async (userId) => {
    const response = await api.get(`/leave-balances/user/${userId}`);
    return response.data;
  },

  getUserLeaveBalance: async (userId, leaveTypeId) => {
    const response = await api.get(`/leave-balances/user/${userId}/leave-type/${leaveTypeId}`);
    return response.data;
  },

  createLeaveBalance: async (userId, leaveTypeId, days) => {
    const response = await api.post('/leave-balances', null, {
      params: { userId, leaveTypeId, days },
    });
    return response.data;
  },

  updateLeaveBalance: async (userId, leaveTypeId, days) => {
    const response = await api.put(`/leave-balances/user/${userId}/leave-type/${leaveTypeId}`, null, {
      params: { days },
    });
    return response.data;
  },

  deleteLeaveBalance: async (userId, leaveTypeId) => {
    const response = await api.delete(`/leave-balances/user/${userId}/leave-type/${leaveTypeId}`);
    return response.data;
  },
};
