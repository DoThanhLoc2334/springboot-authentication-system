import { Card } from "antd";

const AuthPageShell = ({ title, subtitle, children }) => {
  return (
    <div className="auth-page">
      <Card className="auth-card" bordered={false}>
        <div className="auth-brand">
          <h1>{title}</h1>
          <p>{subtitle}</p>
        </div>
        {children}
      </Card>
    </div>
  );
};

export default AuthPageShell;
