# Weather App

Weather App is a robust and user-friendly Android application that provides users with detailed
weekly weather forecasts. Designed with a focus on modern Android development practices, this app
ensures a seamless and efficient user experience by leveraging the MVVM (Model-View-ViewModel)
pattern and Clean Architecture principles.

<img width="33%" src="./screenshot%2F1.HomeScreen.png" alt="Home Screen">

## Key Features

- **Weekly Weather Forecasts**: Users can view a comprehensive weather forecast for the upcoming
  week, including daily temperatures and weather conditions.
- **Coordinate-Based Search**: Users can search for weather information based on specific geographic
  coordinates, making it easy to get accurate weather data for any location.
- **Weather Condition Icons**: Visual icons representing different weather conditions, enhancing the
  user experience by providing quick and intuitive weather status at a glance.
- **Network Error Handling**: The app gracefully handles network errors by allowing users to retry
  fetching data, ensuring a reliable user experience even in less-than-ideal network conditions.
- **Loading and Error States**: Visual indicators for loading and error states enhance user
  experience by providing feedback during data fetching processes.

## Architecture

The application is structured following MVVM and Clean Architecture principles, which helps in
separating concerns and making the codebase more modular, testable, and maintainable.

### Layers

1. **Presentation Layer**:
    - **UI Components**: Built using Jetpack Compose for a modern, declarative UI approach.
    - **ViewModels**: Manage UI-related data and handle interactions between the UI and the domain
      layer, ensuring that the UI remains reactive to data changes.

2. **Domain Layer**:
    - **Use Cases**: Encapsulate business logic and coordinate tasks, ensuring that the app's logic
      is reusable and independent of specific data sources.
    - **Domain Models**: Define the core data structures used throughout the app.
    - **Repository Interfaces**: Abstract the data sources, providing a clean API for the domain
      layer to interact with.
    - **Mappers**: Transform data between domain models and other layers, ensuring a clean
      separation and consistent data handling.

3. **Data Layer**:
    - **Repositories**: Implement the repository interfaces, managing data operations and providing
      a single source of truth for data.
    - **Remote Data Sources**: Handle network operations, fetching weather data from external APIs.
    - **Local Data Sources**: (If applicable) Manage data caching and local storage to improve
      performance and offline capabilities.

## Libraries and Tools

- **Kotlin**: The primary language used for development, offering modern language features and
  enhanced readability.
- **Coroutines**: Simplify asynchronous programming by providing a straightforward way to manage
  background tasks and improve performance.
- **Flow**: Enable reactive programming, allowing the app to manage and react to state changes
  efficiently.
- **Dagger-Hilt**: Facilitate dependency injection, making it easier to manage dependencies and
  improve testability.
- **Jetpack Components**: Including ViewModel and LiveData, which help manage UI-related data in a
  lifecycle-conscious way.
- **Mockito**: Used for unit testing and mocking dependencies, ensuring the app is thoroughly tested
  and reliable.
- **JUnit**: The primary testing framework, used to write and run unit tests.
- **Retrofit**: Simplify network requests and handle RESTful API interactions seamlessly.
- **Gson**: Facilitate JSON parsing, making it easy to handle data serialization and
  deserialization.
