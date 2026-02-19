import axios from 'axios';

// Create axios instance
const api = axios.create({
  baseURL: '/api',  // Vite proxy will forward to http://localhost:8080/api
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor (optional: add auth token later)
api.interceptors.request.use(
  (config) => {
    // Add auth token if exists
    // const token = localStorage.getItem('adminToken');
    // if (token && config.url?.includes('/admin')) {
    //   config.headers['X-API-Token'] = token;
    // }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor (handle errors globally)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

// ========== Public Endpoints ==========
export const blogAPI = {
  // Get all published blogs
  getAllBlogs: (page = 0, size = 10) => {
    return api.get(`/blogs?page=${page}&size=${size}`);
  },

  // Get blog by slug
  getBlogBySlug: (slug) => {
    return api.get(`/blogs/slug/${slug}`);
  }
};

// ========== Admin Endpoints ==========
export const adminAPI = {
  // Create blog
  createBlog: (blogData) => {
    return api.post('/admin/blogs', blogData);
  },

  // Update blog
  updateBlog: (id, blogData) => {
    return api.put(`/admin/blogs/${id}`, blogData);
  },

  // Delete blog
  deleteBlog: (id) => {
    return api.delete(`/admin/blogs/${id}`);
  },

  // Upload image
  uploadImage: (file) => {
    const formData = new FormData();
    formData.append('image', file);
    
    return api.post('/admin/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
};

export default api;