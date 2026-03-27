import { AppstoreOutlined } from "@ant-design/icons";
import { Empty } from "antd";

const Categories = () => {
  return (
    <div>
      <div className="page-section-header">
        <div>
          <h2>Danh muc</h2>
          <p>Khu vuc nay da san sang cho buoc mo rong tiep theo.</p>
        </div>
      </div>

      <Empty
        image={<AppstoreOutlined style={{ fontSize: 48, color: "#9aa3b8" }} />}
        description="Trang quan ly danh muc se duoc bo sung sau."
      />
    </div>
  );
};

export default Categories;
