// Load blog when page loads
document.addEventListener('DOMContentLoaded', loadBlogDetail);

async function loadBlogDetail() {
    const container = document.getElementById('blog-detail');
    
    // Get slug from URL parameter
    const urlParams = new URLSearchParams(window.location.search);
    const slug = urlParams.get('slug');
    
    if (!slug) {
        container.innerHTML = '<p class="error">No blog slug specified.</p>';
        return;
    }
    
    try {
        const blog = await API.getBlogBySlug(slug);
        container.innerHTML = createBlogDetailHTML(blog);
    } catch (error) {
        container.innerHTML = `<p class="error">Failed to load blog: ${error.message}</p>`;
    }
}

// Create blog detail HTML
function createBlogDetailHTML(blog) {
    const date = new Date(blog.publishedAt).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
    
    const tagsHtml = blog.tags && blog.tags.length > 0 
        ? blog.tags.map(tag => `<span class="tag">${tag}</span>`).join('')
        : '';
    
    const imageUrl = blog.featuredImageUrl || 'https://via.placeholder.com/800x400?text=No+Image';
    
    return `
        <div class="blog-detail">
            <img src="${imageUrl}" alt="${blog.title}" style="width: 100%; height: 400px; object-fit: cover; border-radius: 8px;">
            <h1>${blog.title}</h1>
            <div class="blog-detail-meta">
                <span>📅 ${date}</span>
                <div class="blog-tags" style="margin-top: 0;">
                    ${tagsHtml}
                </div>
            </div>
            <div class="blog-detail-content">
                ${blog.content}
            </div>
        </div>
    `;
}