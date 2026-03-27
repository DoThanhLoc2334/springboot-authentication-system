import { Navigate } from "react-router-dom";
import { ROUTES } from "../constants/routes";
import { isAuthenticated } from "../utils/auth";

const ProtectedRoute = ({ children }) => {
  if (!isAuthenticated()) {
    return <Navigate to={ROUTES.login} replace />;
  }

  return children;
};

export default ProtectedRoute;
