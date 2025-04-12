```jsx
import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import MovieList from './components/MovieList';
import ShowtimeList from './components/ShowtimeList';
import SeatSelection from './components/SeatSelection';
import BookingSummary from './components/BookingSummary';
import Confirmation from './components/Confirmation';

const App = () => {
  const [movies, setMovies] = useState([
    { id: 1, title: 'Oppenheimer', genre: 'Drama', imageUrl: 'https://m.media-amazon.com/images/M/MV5BMDBmYTZjNjUtN2M1MS00MTQ2LTk2ODEtNzc2M2QyZGE5N2JkXkEyXkFqcGdeQXVyNzAwMjU2MTY@._V1_QL75_UX190_CR0,0,190,281_.jpg' },
    { id: 2, title: 'Barbie', genre: 'Comedy', imageUrl: 'https://m.media-amazon.com/images/M/MV5BOWRmMGQzMDQtN2Q0Ni00NzE5LTk0MTktNWM0Mjk0MzYwNzE2XkEyXkFqcGdeQXVyMTI0NTA1MDI3._V1_QL75_UX190_CR0,0,190,281_.jpg' },
    { id: 3, title: 'Mission Impossible', genre: 'Action', imageUrl: 'https://m.media-amazon.com/images/M/MV5BYWQ3NzQ1NjktNDY3Mi00ZmMwLTlmOTQtMzkyMmNiZGE4ODFmXkEyXkFqcGdeQXVyNzg3NjQyNQ@@._V1_QL75_UX190_CR0,0,190,281_.jpg' },
  ]);
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [showtimes, setShowtimes] = useState([
    { id: 1, movieId: 1, time: '14:00', screen: 'Screen 1' },
    { id: 2, movieId: 1, time: '17:00', screen: 'Screen 1' },
    { id: 3, movieId: 2, time: '15:00', screen: 'Screen 2' },
    { id: 4, movieId: 3, time: '19:00', screen: 'Screen 3' },
  ]);
  const [selectedShowtime, setSelectedShowtime] = useState(null);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [booking, setBooking] = useState(null);

  const handleSelectMovie = (movie) => {
    setSelectedMovie(movie);
  };

  const handleSelectShowtime = (showtime) => {
    setSelectedShowtime(showtime);
  };

  const handleConfirmSeats = ({ showtime, seats }) => {
    setSelectedSeats(seats);
    setBooking({ movie: selectedMovie, showtime: showtime, seats: seats });
  };

  const handleConfirmBooking = () => {
    // In a real app, you would send the booking data to the server here
    // For this example, we just navigate to the confirmation page
  };

  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<MovieList movies={movies} onSelectMovie={handleSelectMovie} />} />
        <Route path="/showtimes" element={<ShowtimeList showtimes={showtimes.filter((showtime) => showtime.movieId === selectedMovie?.id)} onSelectShowtime={handleSelectShowtime} />} />
        <Route path="/seats" element={<SeatSelection showtime={selectedShowtime} onConfirmSeats={handleConfirmSeats} />} />
        <Route path="/summary" element={<BookingSummary booking={booking} onConfirmBooking={handleConfirmBooking} />} />
        <Route path="/confirmation" element={<Confirmation booking={booking} />} />
      </Routes>
    </Router>
  );
};

export default App;
```