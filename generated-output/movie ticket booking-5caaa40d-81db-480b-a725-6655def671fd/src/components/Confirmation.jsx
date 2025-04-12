```jsx
import React from 'react';

const Confirmation = ({ booking }) => {
  return (
    <div style={{ padding: '20px', textAlign: 'center' }}>
      <h2>Booking Confirmed!</h2>
      <p>Thank you for your booking.</p>
      <p><strong>Movie:</strong> {booking.movie.title}</p>
      <p><strong>Showtime:</strong> {booking.showtime.time}, Screen {booking.showtime.screen}</p>
      <p><strong>Seats:</strong> {booking.seats.join(', ')}</p>
      <p>A confirmation email has been sent to your address.</p>
    </div>
  );
};

export default Confirmation;
```