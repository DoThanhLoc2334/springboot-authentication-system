import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./pages/Login";

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          {/* Trang mặc định sẽ tự động chuyển hướng sang /login */}
          <Route path="/" element={<Navigate to="/login" />} />

          {/* Định nghĩa đường dẫn cho trang Login */}
          <Route path="/login" element={<Login />} />

          {/* Sau này Huy sẽ thêm các trang Dashboard, Category ở đây */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
