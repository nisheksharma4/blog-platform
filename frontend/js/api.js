// Debug: Check if file is loaded
console.log('api.js loaded successfully');

// API Configuration
const API_BASE = 'http://localhost:8080/api';

// API Service Object
const API = {
    // Public endpoints
    getAllBlogs: async function(page = 0, size = 10) {
        console.log('Fetching blogs from:', `${API_BASE}/blogs?page=${page}&size=${size}`);
        
        const response = await fetch(`${API_BASE}/blogs?page=${page}&size=${size}`);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        console.log('Full response from backend:', data);
        
        // Backend uses "body" field, not "data"
        if (data.body) {
            console.log('Found blogs in data.body:', data.body);
            return data.body;
        } else if (data.data) {
            console.log('Found blogs in data.data:', data.data);
            return data.data;
        } else {
            console.error('Unexpected response structure');
            throw new Error('Unexpected response format from server');
        }
    },

    getBlogBySlug: async function(slug) {
        console.log('Fetching blog by slug:', slug);
        
        const response = await fetch(`${API_BASE}/blogs/slug/${slug}`);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        console.log('Blog response:', data);
        
        // Backend uses "body" field
        return data.body || data.data || data;
    },

    // Admin endpoints
    createBlog: async function(blogData) {
        console.log('Creating blog:', blogData);
        
        const response = await fetch(`${API_BASE}/admin/blogs`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(blogData)
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to create blog');
        }
        
        const data = await response.json();
        console.log('Blog created:', data);
        return data.body || data.data || data;
    },

    updateBlog: async function(id, blogData) {
        console.log('Updating blog:', id);
        
        const response = await fetch(`${API_BASE}/admin/blogs/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(blogData)
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to update blog');
        }
        
        const data = await response.json();
        console.log('Blog updated:', data);
        return data.body || data.data || data;
    },

    deleteBlog: async function(id) {
        console.log('Deleting blog:', id);
        
        const response = await fetch(`${API_BASE}/admin/blogs/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to delete blog');
        }
        
        console.log('Blog deleted');
        return true;
    }
};

// Debug: Check if API object is created
console.log('API object created:', API);