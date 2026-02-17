// Debug: Check if main.js loaded
console.log('main.js loaded');

// Wait for DOM to be ready
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, calling loadBlogs()');
    loadBlogs();
});

async function loadBlogs() {
    const container = document.getElementById('blogs-container');
    
    // Debug: Check if API exists
    if (typeof API === 'undefined') {
        console.error('API is not defined!');
        container.innerHTML = '<p class="error">API service not loaded. Check browser console for errors.</p>';
        return;
    }
    
    console.log('API is defined, fetching blogs...');
    
    try {
        const blogs = await API.getAllBlogs(0, 10);
        
        console.log('📚 Received blogs:', blogs);
        
        // Check if blogs is an array
        if (!Array.isArray(blogs)) {
            console.error('Blogs is not an array:', blogs);
            container.innerHTML = '<p class="error">Unexpected response format from server.</p>';
            return;
        }
        
        if (blogs.length === 0) {
            container.innerHTML = '<p>No blogs published yet. Check back later!</p>';
            return;
        }
        
        container.innerHTML = blogs.map(blog => createBlogCard(blog)).join('');
    } catch (error) {
        console.error('Error loading blogs:', error);
        container.innerHTML = `<p class="error">Failed to load blogs: ${error.message}</p>`;
    }
}

// Create blog card HTML
function createBlogCard(blog) {
    console.log('Creating card for blog:', blog);
    
    const date = blog.publishedAt 
        ? new Date(blog.publishedAt).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        })
        : 'No date';
    
    const tagsHtml = blog.tags && blog.tags.length > 0 
        ? blog.tags.map(tag => `<span class="tag">${tag}</span>`).join('')
        : '';
    
    const imageUrl = blog.featuredImageUrl || 'https://via.placeholder.com/800x400?text=No+Image';
    
    return `
        <div class="blog-card">
            <img src="${imageUrl}" alt="${blog.title || 'Blog'}">
            <div class="blog-card-content">
                <h3><a href="blog-detail.html?slug=${blog.slug}" style="color: inherit; text-decoration: none;">${blog.title || 'Untitled'}</a></h3>
                <p>${blog.excerpt || (blog.content ? blog.content.substring(0, 150) : 'No content')}${blog.content && blog.content.length > 150 ? '...' : ''}</p>
                <div class="blog-meta">
                    <span>📅 ${date}</span>
                    <a href="blog-detail.html?slug=${blog.slug}" style="color: #667eea;">Read More →</a>
                </div>
                <div class="blog-tags">
                    ${tagsHtml}
                </div>
            </div>
        </div>
    `;
}