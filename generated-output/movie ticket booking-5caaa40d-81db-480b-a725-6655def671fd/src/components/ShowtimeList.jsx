```jsx
import React from 'react';

const ShowtimeList = ({ showtimes, onSelectShowtime }) => {
  return (
    <div style={{ padding: '20px' }}>
      <h2>Available Showtimes</h2>
      <div style={{ display: 'flex', flexDirection: 'column' }}>
        {showtimes.map((showtime) => (
          <div key={showtime.id} style={{ border: '1px solid #ccc', padding: '10px', margin: '10px', borderRadius: '5px' }}>
            <p><strong>Time:</strong> {showtime.time}</p>
            <p><strong>Screen:</strong> {showtime.screen}</p>
            <button onClick={() => onSelectShowtime(showtime)} style={{ backgroundColor: '#008CBA', color: 'white', padding: '8px 16px', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
              Select Seats
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ShowtimeList;
```