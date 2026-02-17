// API Configuration
const API_BASE = 'http://localhost:8080/api';

// API Service Object
const API = {
    // Public endpoints
    async getAllBlogs(page = 0, size = 10) {
        try {
            const response = await fetch(`${API_BASE}/blogs?page=${page}&size=${size}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.data; // Return the data array
        } catch (error) {
            console.error('Error fetching blogs:', error);
            throw error;
        }
    },

    async getBlogBySlug(slug) {
        try {
            const response = await fetch(`${API_BASE}/blogs/slug/${slug}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.data;
        } catch (error) {
            console.error('Error fetching blog by slug:', error);
            throw error;
        }
    },

    // Admin endpoints
    async createBlog(blogData) {
        try {
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
            return data.data;
        } catch (error) {
            console.error('Error creating blog:', error);
            throw error;
        }
    },

    async updateBlog(id, blogData) {
        try {
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
            return data.data;
        } catch (error) {
            console.error('Error updating blog:', error);
            throw error;
        }
    },

    async deleteBlog(id) {
        try {
            const response = await fetch(`${API_BASE}/admin/blogs/${id}`, {
                method: 'DELETE'
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to delete blog');
            }
            return true;
        } catch (error) {
            console.error('Error deleting blog:', error);
            throw error;
        }
    }
};