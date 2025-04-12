```jsx
import React from 'react';

const MovieCard = ({ movie, onSelect }) => {
  return (
    <div style={{ width: '200px', margin: '20px', border: '1px solid #ccc', borderRadius: '5px', padding: '10px', textAlign: 'center' }}>
      <img src={movie.imageUrl} alt={movie.title} style={{ width: '100%', height: 'auto', marginBottom: '10px' }} />
      <h3>{movie.title}</h3>
      <p>{movie.genre}</p>
      <button onClick={() => onSelect(movie)} style={{ backgroundColor: '#4CAF50', color: 'white', padding: '8px 16px', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
        View Showtimes
      </button>
    </div>
  );
};

export default MovieCard;
```