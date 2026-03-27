import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import AdminLayout from "./components/AdminLayout";
import ProtectedRoute from "./components/ProtectedRoute";
import ProductList from "./pages/ProductList";
import Register from "./pages/Register";

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Route: Ai cũng vào được */}
        <Route path="/login" element={<Login />} />
        <Route path="register" element={<Register />} />

        {/* Protected Admin Routes: Chỉ dành cho người có Token */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute>
              <AdminLayout />
            </ProtectedRoute>
          }
        >
          {/* Các trang con sẽ được hiển thị tại vị trí của <Outlet /> trong AdminLayout */}
          <Route path="dashboard" element={<Dashboard />} />
          <Route
            path="categories"
            element={<div>Trang Danh mục (Sẽ làm sau)</div>}
          />
          <Route path="products" element={<ProductList />} />
        </Route>

        {/* Mặc định: Nếu vào "/" hoặc trang lạ, đá về /admin/dashboard */}
        <Route path="*" element={<Navigate to="/admin/dashboard" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
