import { Typography, message } from "antd";
import { useEffect, useState } from "react";
import api from "../api/api";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const Dashboard = () => {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const response = await api.get("/users/profile");
        setProfile(response.data);
      } catch (error) {
        message.error(
          getApiErrorMessage(error, "Could not load account information."),
        );
      }
    };

    void loadProfile();
  }, []);

  const summaryCards = [
    {
      title: "Account",
      value: profile?.username ?? "Loading",
      description: profile?.email ?? "Current account information.",
    },
    {
      title: "Authorization",
      value: profile?.roles?.includes("ROLE_ADMIN") ? "ADMIN" : "USER",
      description: "The current role is loaded directly from the backend.",
    },
    {
      title: "Security",
      value: "JWT",
      description: "Use the header action to update your password.",
    },
  ];

  return (
    <div>
      <div className="page-section-header">
        <div>
          <h2>Admin Dashboard</h2>
          <p>Quickly monitor user management and authorization status.</p>
        </div>
      </div>

      <div className="dashboard-grid">
        {summaryCards.map((card) => (
          <section className="dashboard-card" key={card.title}>
            <span className="dashboard-card__label">{card.title}</span>
            <strong className="dashboard-card__value">{card.value}</strong>
            <p>{card.description}</p>
          </section>
        ))}
      </div>
    </div>
  );
};

export default Dashboard;
