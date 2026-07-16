import api from './api';

export const leaveRequestService = {
  getAllLeaveRequests: async () => {
    const response = await api.get('/leaves');
    return response.data;
  },

  getLeaveRequestById: async (id) => {
    const response = await api.get(`/leaves/${id}`);
    return response.data;
  },

  getMyLeaveRequests: async () => {
    const response = await api.get('/leaves/my-requests');
    return response.data;
  },

  getTeamLeaveRequests: async () => {
    const response = await api.get('/leaves/team-requests');
    return response.data;
  },

  applyForLeave: async (leaveData) => {
    const response = await api.post('/leaves/apply', leaveData);
    return response.data;
  },

  approveLeave: async (id, comments) => {
    const response = await api.put(`/leaves/${id}/approve`, { comments });
    return response.data;
  },

  rejectLeave: async (id, comments) => {
    const response = await api.put(`/leaves/${id}/reject`, { comments });
    return response.data;
  },

  cancelLeave: async (id) => {
    const response = await api.put(`/leaves/${id}/cancel`);
    return response.data;
  },

  getLeaveRequestsByStatus: async (status) => {
    const response = await api.get(`/leaves/status/${status}`);
    return response.data;
  },
};
