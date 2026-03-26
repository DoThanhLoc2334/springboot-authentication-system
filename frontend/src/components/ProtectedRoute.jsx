import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("token");

  if (!token) {
    // Nếu không có thẻ, mời về trang Login
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
