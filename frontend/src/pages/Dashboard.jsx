const summaryCards = [
  {
    title: "Products",
    value: "120",
    description: "Dang duoc quan ly trong he thong.",
  },
  {
    title: "Orders",
    value: "45",
    description: "Don hang moi trong ngay hom nay.",
  },
  {
    title: "Categories",
    value: "8",
    description: "Nhom san pham san sang de mo rong.",
  },
];

const Dashboard = () => {
  return (
    <div>
      <div className="page-section-header">
        <div>
          <h2>Admin dashboard</h2>
          <p>Theo doi nhanh tinh hinh van hanh cua cua hang.</p>
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
