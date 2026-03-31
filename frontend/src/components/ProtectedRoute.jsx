import { Navigate } from "react-router-dom";
import { ROUTES } from "../constants/routes";
import { isAdmin, isAuthenticated } from "../utils/auth";

const ProtectedRoute = ({ children, requireAdmin = false }) => {
  if (!isAuthenticated()) {
    return <Navigate to={ROUTES.login} replace />;
  }

  if (requireAdmin && !isAdmin()) {
    return <Navigate to={ROUTES.dashboard} replace />;
  }

  return children;
};

export default ProtectedRoute;
