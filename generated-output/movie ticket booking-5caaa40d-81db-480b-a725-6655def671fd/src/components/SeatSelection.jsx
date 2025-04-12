```jsx
import React, { useState } from 'react';

const SeatSelection = ({ showtime, onConfirmSeats }) => {
  const [selectedSeats, setSelectedSeats] = useState([]);

  const handleSeatClick = (seatNumber) => {
    if (selectedSeats.includes(seatNumber)) {
      setSelectedSeats(selectedSeats.filter((seat) => seat !== seatNumber));
    } else {
      setSelectedSeats([...selectedSeats, seatNumber]);
    }
  };

  const handleConfirm = () => {
    onConfirmSeats({ showtime, seats: selectedSeats });
  };

  // Assuming a simple 5x10 seating arrangement
  const rows = 5;
  const seatsPerRow = 10;
  const totalSeats = rows * seatsPerRow;

  return (
    <div style={{ padding: '20px' }}>
      <h2>Select Your Seats</h2>
      <p>Showtime: {showtime.time}, Screen: {showtime.screen}</p>
      <div style={{ display: 'grid', gridTemplateColumns: `repeat(${seatsPerRow}, 30px)`, gap: '5px' }}>
        {Array.from({ length: totalSeats }, (_, i) => i + 1).map((seatNumber) => (
          <button
            key={seatNumber}
            onClick={() => handleSeatClick(seatNumber)}
            style={{
              backgroundColor: selectedSeats.includes(seatNumber) ? 'green' : 'lightgray',
              padding: '5px',
              border: 'none',
              cursor: 'pointer',
              borderRadius: '3px',
            }}
          >
            {seatNumber}
          </button>
        ))}
      </div>
      <button onClick={handleConfirm} style={{ backgroundColor: '#4CAF50', color: 'white', padding: '10px 20px', border: 'none', borderRadius: '5px', cursor: 'pointer', marginTop: '20px' }}>
        Confirm Seats
      </button>
    </div>
  );
};

export default SeatSelection;
```