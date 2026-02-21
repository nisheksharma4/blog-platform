import { useState, useEffect } from 'react'
import { Routes, Route, Link, useNavigate, useParams } from 'react-router-dom'
import { blogAPI } from './services/api'

// ========== HOME PAGE ==========
function HomePage() {
  const [blogs, setBlogs] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    blogAPI.getAllBlogs(0, 10)
      .then(response => {
        setBlogs(response.data.body || [])
        setLoading(false)
      })
      .catch(err => {
        console.error('Error:', err)
        setLoading(false)
      })
  }, [])

  if (loading) return <div className="App"><p>Loading blogs...</p></div>

  return (
    <div className="App">
      <nav style={{ padding: '1rem', background: '#667eea', textAlign: 'center' }}>
        <Link to="/" style={{ color: 'white', margin: '0 15px', textDecoration: 'none', fontSize: '1.1rem' }}>Home</Link>
        <Link to="/admin" style={{ color: 'white', margin: '0 15px', textDecoration: 'none', fontSize: '1.1rem' }}>Admin</Link>
      </nav>
      
      <h1 style={{ textAlign: 'center', margin: '2rem 0' }}>📝 Nishek's Blog</h1>
      
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px', padding: '0 20px' }}>
        {blogs.map(blog => (
          <div key={blog.id} style={{
            border: '1px solid #ddd',
            borderRadius: '8px',
            padding: '15px',
            backgroundColor: '#fff',
            boxShadow: '0 2px 5px rgba(0,0,0,0.1)'
          }}>
            <h3>
              <Link 
                to={`/blog/${blog.slug}`} 
                style={{ color: '#667eea', textDecoration: 'none' }}
              >
                {blog.title}
              </Link>
            </h3>
            <p>{blog.excerpt || blog.content?.substring(0, 100)}...</p>
            <small>📅 {new Date(blog.publishedAt).toLocaleDateString()}</small>
          </div>
        ))}
      </div>
    </div>
  )
}

// ========== BLOG DETAIL PAGE ==========
function BlogDetailPage() {
  const [blog, setBlog] = useState(null)
  const [loading, setLoading] = useState(true)
  const { slug } = useParams() // Get slug from URL: /blog/:slug
  const navigate = useNavigate()

  useEffect(() => {
    if (!slug) return
    
    blogAPI.getBlogBySlug(slug)
      .then(response => {
        setBlog(response.data.body || null)
        setLoading(false)
      })
      .catch(err => {
        console.error('Blog not found:', err)
        alert('Blog not found!')
        navigate('/') // Go back to home if blog not found
      })
  }, [slug, navigate])

  if (loading) return <div className="App"><p>Loading blog...</p></div>
  if (!blog) return null

  return (
    <div className="App">
      <nav style={{ padding: '1rem', background: '#667eea', textAlign: 'center' }}>
        <button 
          onClick={() => navigate('/')}
          style={{ 
            background: 'none', 
            border: 'none', 
            color: 'white', 
            fontSize: '1.1rem',
            cursor: 'pointer',
            textDecoration: 'underline'
          }}
        >
          ← Back to Home
        </button>
      </nav>
      
      <article style={{ maxWidth: '800px', margin: '2rem auto', padding: '0 20px' }}>
        <h1>{blog.title}</h1>
        <p style={{ color: '#666', margin: '10px 0 20px' }}>
          📅 {new Date(blog.publishedAt).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
          })}
        </p>
        
        {blog.featuredImageUrl && (
          <img 
            src={blog.featuredImageUrl} 
            alt={blog.title}
            style={{ 
              width: '100%', 
              height: '300px', 
              objectFit: 'cover', 
              borderRadius: '8px', 
              marginBottom: '25px' 
            }}
            onError={(e) => {
              e.target.src = 'https://via.placeholder.com/800x400?text=Blog+Image'
            }}
          />
        )}
        
        <div 
          style={{ 
            lineHeight: '1.7',
            fontSize: '1.05rem'
          }}
          dangerouslySetInnerHTML={{ __html: blog.content }}
        />
      </article>
    </div>
  )
}

// ========== ADMIN PAGE (Placeholder) ==========
function AdminPage() {
  return (
    <div className="App">
      <nav style={{ padding: '1rem', background: '#667eea', textAlign: 'center' }}>
        <button 
          onClick={() => window.history.back()}
          style={{ 
            background: 'none', 
            border: 'none', 
            color: 'white', 
            fontSize: '1.1rem',
            cursor: 'pointer',
            textDecoration: 'underline'
          }}
        >
          ← Back
        </button>
      </nav>
      
      <div style={{ maxWidth: '600px', margin: '3rem auto', textAlign: 'center' }}>
        <h1>🔐 Admin Panel</h1>
        <p style={{ marginTop: '20px', fontSize: '1.1rem' }}>
          Coming soon: Create and manage your blog posts
        </p>
      </div>
    </div>
  )
}

// ========== MAIN APP WITH ROUTES ==========
function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/blog/:slug" element={<BlogDetailPage />} />
      <Route path="/admin" element={<AdminPage />} />
    </Routes>
  )
}

export default App