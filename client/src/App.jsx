import { useState, useEffect } from 'react'
import { blogAPI } from './services/api'
import './App.css'

function App() {
  const [blogs, setBlogs] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    // Using axios via blogAPI
    blogAPI.getAllBlogs(0, 10)
      .then(response => {
        // Your backend returns: { status, message, body: [...] }
        setBlogs(response.data.body || [])
        setLoading(false)
      })
      .catch(err => {
        console.error('API Error:', err)
        setError(err.message || 'Failed to load blogs')
        setLoading(false)
      })
  }, [])

  if (loading) return <div className="container">Loading blogs...</div>
  if (error) return <div className="container" style={{ color: 'red' }}>❌ Error: {error}</div>

  return (
    <div className="App">
      <div className="container">
        <h1>📝 Nishek's Blog</h1>
        
        {blogs.length === 0 ? (
          <p>No blogs published yet. Create one from admin panel!</p>
        ) : (
          <div className="blogs-grid">
            {blogs.map(blog => (
              <div key={blog.id} className="blog-card">
                {blog.featuredImageUrl && (
                  <img 
                    src={blog.featuredImageUrl} 
                    alt={blog.title}
                    className="blog-image"
                    onError={(e) => {
                      e.target.src = 'https://via.placeholder.com/800x400?text=No+Image'
                    }}
                  />
                )}
                <div className="blog-content">
                  <h2>{blog.title}</h2>
                  <p className="excerpt">
                    {blog.excerpt || (blog.content ? blog.content.substring(0, 150) + '...' : 'No excerpt')}
                  </p>
                  <div className="meta">
                    <span>📅 {new Date(blog.publishedAt).toLocaleDateString()}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default App