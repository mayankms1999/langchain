` for files and `<folder>{path here}</folder>` for folder creation instructions.

Here's the plan:

1.  **Project Setup:** Assume Vite and React are already set up. If not, a simple `npm create vite@latest movie-booking --template react` will get you started.
2.  **Components:** Break down the UI into reusable components:
    *   `Header`: App header with logo and title.
    *   `MovieList`: Displays a list of movies.
    *   `MovieCard`: Displays individual movie details.
    *   `ShowtimeList`: Displays available showtimes for a selected movie.
    *   `SeatSelection`: Allows users to select seats for a showtime.
    *   `BookingSummary`: Displays a summary of the booking.
    *   `Confirmation`: Confirmation page after booking.
3.  **Data:** Use placeholder data for movies and showtimes. A real app would fetch this from an API.
4.  **State Management:** Use React's `useState` for managing the selected movie, showtime, seats, and booking confirmation. For more complex state management, consider Redux or Context API in a larger application.
5.  **Styling:** Use simple inline styles or basic CSS. For a production app, consider using a CSS framework like Tailwind CSS or Material UI.
6.  **Routing:** Implement basic routing using `react-router-dom` to navigate between movie listings, showtimes, seat selection, and confirmation pages.

Here's the code:

<folder>src/components</folder>

<action fileName="src/components/Header.jsx">
```jsx
import React from 'react';

const Header = () => {
  return (
    <header style={{ backgroundColor: '#333', color: 'white', padding: '20px', textAlign: 'center' }}>
      <h1>Movie Ticket Booking</h1>
    </header>
  );
};

export default Header;
```