# Popular Movies APP
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. 
This app allows users to discover the most popular movies playing. 

The app:

- Presents the user with a grid arrangement of movie posters upon launch.
- Allows your user to change sort order via a setting:
  -The sort order can be by most popular or by highest-rated.
  
- Allows the user to tap on a movie poster and transition to a details screen with additional information such as:
  -original title
  -movie poster image thumbnail
  -A plot synopsis (called overview in the api)
  -user rating (called vote_average in the api)
  -release date

Besides, It:
- Allows users to view and play trailers ( either in the youtube app or a web browser).
- Allows users to read reviews of a selected movie.
- Allows users to mark a movie as a favorite in the details view by tapping a button(star).
- Creates a database to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).

This project uses:
- Retrofit Library: to fetch data from the Internet with theMovieDB API.
- Adapters and custom list layouts to populate list views.
