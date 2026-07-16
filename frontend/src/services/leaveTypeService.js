import api from './api';

export const leaveTypeService = {
  getAllLeaveTypes: async () => {
    const response = await api.get('/leave-types');
    return response.data;
  },

  getLeaveTypeById: async (id) => {
    const response = await api.get(`/leave-types/${id}`);
    return response.data;
  },

  createLeaveType: async (leaveTypeData) => {
    const response = await api.post('/leave-types', leaveTypeData);
    return response.data;
  },

  updateLeaveType: async (id, leaveTypeData) => {
    const response = await api.put(`/leave-types/${id}`, leaveTypeData);
    return response.data;
  },

  deleteLeaveType: async (id) => {
    const response = await api.delete(`/leave-types/${id}`);
    return response.data;
  },
};
