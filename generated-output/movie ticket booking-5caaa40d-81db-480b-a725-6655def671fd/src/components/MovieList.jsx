```jsx
import React from 'react';
import MovieCard from './MovieCard';

const MovieList = ({ movies, onSelectMovie }) => {
  return (
    <div style={{ padding: '20px' }}>
      <h2>Now Showing</h2>
      <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-around' }}>
        {movies.map((movie) => (
          <MovieCard key={movie.id} movie={movie} onSelect={onSelectMovie} />
        ))}
      </div>
    </div>
  );
};

export default MovieList;
```