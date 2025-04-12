```jsx
import React from 'react';

const BookingSummary = ({ booking, onConfirmBooking }) => {
  return (
    <div style={{ padding: '20px' }}>
      <h2>Booking Summary</h2>
      <p><strong>Movie:</strong> {booking.movie.title}</p>
      <p><strong>Showtime:</strong> {booking.showtime.time}, Screen {booking.showtime.screen}</p>
      <p><strong>Seats:</strong> {booking.seats.join(', ')}</p>
      <p><strong>Total:</strong> ${booking.seats.length * 15}</p>
      <button onClick={onConfirmBooking} style={{ backgroundColor: '#4CAF50', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
        Confirm Booking
      </button>
    </div>
  );
};

export default BookingSummary;
```