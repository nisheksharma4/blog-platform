let editingBlogId = null;

// Load blogs and setup form when page loads
document.addEventListener('DOMContentLoaded', () => {
    loadAdminBlogs();
    setupForm();
});

// Setup form event listeners
function setupForm() {
    const form = document.getElementById('blog-form');
    const cancelBtn = document.getElementById('cancel-btn');
    
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        await handleSubmit();
    });
    
    cancelBtn.addEventListener('click', () => {
        resetForm();
    });
}

// Handle form submission (create or update)
async function handleSubmit() {
    const blogData = getFormData();
    
    try {
        if (editingBlogId) {
            // Update existing blog
            await API.updateBlog(editingBlogId, blogData);
            showNotification('Blog updated successfully!', 'success');
        } else {
            // Create new blog
            await API.createBlog(blogData);
            showNotification('Blog created successfully!', 'success');
        }
        
        // Reset form and reload blog list
        resetForm();
        loadAdminBlogs();
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

// Get form data
function getFormData() {
    const tagsInput = document.getElementById('tags').value;
    const tags = tagsInput 
        ? tagsInput.split(',').map(tag => tag.trim()).filter(tag => tag)
        : [];
    
    return {
        title: document.getElementById('title').value,
        slug: document.getElementById('slug').value || null,
        excerpt: document.getElementById('excerpt').value || null,
        content: document.getElementById('content').value,
        tags: tags,
        featuredImageUrl: document.getElementById('featuredImageUrl').value || null
    };
}

// Reset form to create mode
function resetForm() {
    document.getElementById('blog-form').reset();
    document.getElementById('blog-id').value = '';
    document.getElementById('form-title').textContent = 'Create New Blog';
    document.getElementById('submit-btn').textContent = 'Create Blog';
    document.getElementById('cancel-btn').style.display = 'none';
    editingBlogId = null;
}

// Load blogs for admin panel
async function loadAdminBlogs() {
    const container = document.getElementById('admin-blogs-container');
    
    try {
        const blogs = await API.getAllBlogs(0, 50); // Get all blogs
        
        if (blogs.length === 0) {
            container.innerHTML = '<p>No blogs yet. Create one above!</p>';
            return;
        }
        
        container.innerHTML = blogs.map(blog => createAdminBlogItem(blog)).join('');
    } catch (error) {
        container.innerHTML = `<p class="error">Failed to load blogs: ${error.message}</p>`;
    }
}

// Create admin blog item HTML
function createAdminBlogItem(blog) {
    return `
        <div class="admin-blog-item">
            <div class="admin-blog-info">
                <h4>${blog.title}</h4>
                <div class="blog-slug">/${blog.slug}</div>
            </div>
            <div class="admin-blog-actions">
                <button class="edit-btn" onclick="editBlog('${blog.id}')">Edit</button>
                <button class="delete-btn" onclick="deleteBlog('${blog.id}')">Delete</button>
            </div>
        </div>
    `;
}

// Edit blog (fill form with blog data)
async function editBlog(id) {
    try {
        const blog = await API.getBlogBySlug(
            document.querySelector(`[onclick="editBlog('${id}')"]`).closest('.admin-blog-item').querySelector('.blog-slug').textContent.substring(1)
        );
        
        // Fill form
        document.getElementById('blog-id').value = blog.id;
        document.getElementById('title').value = blog.title;
        document.getElementById('slug').value = blog.slug;
        document.getElementById('excerpt').value = blog.excerpt || '';
        document.getElementById('content').value = blog.content;
        document.getElementById('tags').value = blog.tags?.join(', ') || '';
        document.getElementById('featuredImageUrl').value = blog.featuredImageUrl || '';
        
        // Update UI
        document.getElementById('form-title').textContent = 'Edit Blog';
        document.getElementById('submit-btn').textContent = 'Update Blog';
        document.getElementById('cancel-btn').style.display = 'inline-block';
        editingBlogId = blog.id;
        
        // Scroll to form
        document.querySelector('.admin-section:first-child').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        showNotification('Failed to load blog for editing: ' + error.message, 'error');
    }
}

// Delete blog
async function deleteBlog(id) {
    if (!confirm('Are you sure you want to delete this blog?')) {
        return;
    }
    
    try {
        await API.deleteBlog(id);
        showNotification('Blog deleted successfully!', 'success');
        loadAdminBlogs();
    } catch (error) {
        showNotification('Failed to delete blog: ' + error.message, 'error');
    }
}

// Show notification
function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = type;
    notification.textContent = message;
    notification.style.position = 'fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.padding = '1rem';
    notification.style.borderRadius = '6px';
    notification.style.zIndex = '1000';
    notification.style.minWidth = '300px';
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 3000);
}