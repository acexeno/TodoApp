# Todo Manager App

A modern, responsive Todo Manager application built with Django that works on both web browsers and mobile emulators.

## Features

- Create, read, update, and delete todo items
- Mark todos as complete/incomplete
- Due date management
- Priority levels
- Responsive design for both web and mobile
- User authentication
- Clean and modern UI

## Setup Instructions

1. Create a virtual environment:
```bash
python -m venv venv
```

2. Activate the virtual environment:
- Windows:
```bash
venv\Scripts\activate
```
- Unix/MacOS:
```bash
source venv/bin/activate
```

3. Install dependencies:
```bash
pip install -r requirements.txt
```

4. Run migrations:
```bash
python manage.py migrate
```

5. Create a superuser (admin):
```bash
python manage.py createsuperuser
```

6. Run the development server:

python manage.py runserver


7. Access the application:
- Web: http://localhost:8000
- Admin panel: http://localhost:8000/admin

## Mobile Testing

To test on mobile emulators:
1. Run the development server
2. Access the application using your computer's local IP address (e.g., http://192.168.1.xxx:8000)
3. Use mobile emulators or real devices on the same network to access the application

## Technologies Used

- Python 3.x
- Django 5.0.2
- Bootstrap 5
- Django Crispy Forms
- SQLite (development) / PostgreSQL (production)
- HTML5/CSS3/JavaScript 

# Todo Manager Mobile App

A React Native mobile application for managing todo items, built to work with the Django backend.

## Features

- User authentication (login/register)
- Create, read, update, and delete todo items
- Toggle todo completion status
- User profile management
- Modern and intuitive UI
- Offline support with AsyncStorage
- Secure token-based authentication

## Prerequisites

- Node.js (v14 or later)
- npm or yarn
- React Native development environment set up
- Android Studio (for Android development)
- Xcode (for iOS development, macOS only)

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd TodoManagerMobile
```

2. Install dependencies:
```bash
npm install
# or
yarn install
```

3. Update the API URL:
Open `src/services/api.ts` and update the `API_URL` constant with your Django backend server address.

## Running the App

### Android

1. Start an Android emulator or connect a physical device
2. Run the app:
```bash
npm run android
# or
yarn android
```

### iOS (macOS only)

1. Install iOS dependencies:
```bash
cd ios
pod install
cd ..
```

2. Start an iOS simulator or connect a physical device
3. Run the app:
```bash
npm run ios
# or
yarn ios
```

## Project Structure

```
src/
├── components/         # Reusable UI components
├── hooks/             # Custom React hooks
├── navigation/        # Navigation configuration
├── screens/           # Screen components
├── services/          # API and other services
└── utils/             # Utility functions
```

## Available Scripts

- `npm start` or `yarn start` - Start the Metro bundler
- `npm run android` or `yarn android` - Run the app on Android
- `npm run ios` or `yarn ios` - Run the app on iOS
- `npm test` or `yarn test` - Run tests
- `npm run lint` or `yarn lint` - Run ESLint

## Dependencies

- @react-navigation/native - Navigation library
- @react-navigation/native-stack - Stack navigation
- @react-navigation/bottom-tabs - Tab navigation
- axios - HTTP client
- @react-native-async-storage/async-storage - Local storage
- react-native-safe-area-context - Safe area handling
- react-native-screens - Native navigation primitives
- react-native-vector-icons - Icon library

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 