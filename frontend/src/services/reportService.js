import api from './api';

export const reportService = {
  getLeaveSummary: async () => {
    const response = await api.get('/reports/leave-summary');
    return response.data;
  },

  getDepartmentSummary: async () => {
    const response = await api.get('/reports/department-summary');
    return response.data;
  },

  getMonthlyReport: async (year, month) => {
    const response = await api.get('/reports/monthly-report', {
      params: { year, month },
    });
    return response.data;
  },

  exportToCsv: async () => {
    const response = await api.get('/reports/export-csv', {
      responseType: 'blob',
    });
    return response.data;
  },
};
