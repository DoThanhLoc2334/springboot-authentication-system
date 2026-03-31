import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminLayout from "./components/AdminLayout";
import ProtectedRoute from "./components/ProtectedRoute";
import { ROUTES } from "./constants/routes";
import Categories from "./pages/Categories";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login";
import ProductList from "./pages/ProductList";
import Register from "./pages/Register";
import Users from "./pages/Users";
import { isAuthenticated } from "./utils/auth";

function App() {
  const defaultRoute = isAuthenticated() ? ROUTES.dashboard : ROUTES.login;

  return (
    <BrowserRouter>
      <Routes>
        <Route path={ROUTES.login} element={<Login />} />
        <Route path={ROUTES.register} element={<Register />} />
        <Route
          path={ROUTES.admin}
          element={
            <ProtectedRoute>
              <AdminLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<Navigate to={ROUTES.dashboard} replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="categories" element={<Categories />} />
          <Route path="products" element={<ProductList />} />
          <Route
            path="users"
            element={
              <ProtectedRoute requireAdmin>
                <Users />
              </ProtectedRoute>
            }
          />
        </Route>
        <Route path="/" element={<Navigate to={defaultRoute} replace />} />
        <Route path="*" element={<Navigate to={defaultRoute} replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
