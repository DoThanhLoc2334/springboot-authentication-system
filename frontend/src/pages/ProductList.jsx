import { PlusOutlined } from "@ant-design/icons";
import {
  Button,
  Image,
  Popconfirm,
  Space,
  Table,
  Tag,
  Typography,
  message,
} from "antd";
import { useEffect, useState } from "react";
import api from "../api/api";
import ProductFormModal from "../components/ProductFormModal";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const { Title, Paragraph } = Typography;
const currencyFormatter = new Intl.NumberFormat("vi-VN");

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

  const loadData = async () => {
    setLoading(true);

    try {
      const [productResponse, categoryResponse] = await Promise.all([
        api.get("/products"),
        api.get("/categories"),
      ]);

      setProducts(productResponse.data);
      setCategories(categoryResponse.data);
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Khong the tai danh sach san pham."),
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadData();
  }, []);

  const closeModal = () => {
    setModalOpen(false);
    setEditingProduct(null);
  };

  const handleCreateClick = () => {
    setEditingProduct(null);
    setModalOpen(true);
  };

  const handleEditClick = (product) => {
    setEditingProduct(product);
    setModalOpen(true);
  };

  const handleSave = async (values) => {
    setSaving(true);

    try {
      if (editingProduct) {
        await api.put(`/products/${editingProduct.id}`, values);
        message.success("Cap nhat san pham thanh cong.");
      } else {
        await api.post("/products", values);
        message.success("Them san pham thanh cong.");
      }

      closeModal();
      await loadData();
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Khong the luu san pham. Vui long thu lai."),
      );
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/products/${id}`);
      message.success("Xoa san pham thanh cong.");
      await loadData();
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Khong the xoa san pham nay."),
      );
    }
  };

  const columns = [
    {
      title: "Hinh anh",
      dataIndex: "imageUrl",
      key: "imageUrl",
      width: 100,
      render: (imageUrl) => (
        <Image
          src={imageUrl}
          width={56}
          height={56}
          style={{ objectFit: "cover", borderRadius: 12 }}
          fallback="https://via.placeholder.com/56?text=N/A"
        />
      ),
    },
    {
      title: "San pham",
      dataIndex: "name",
      key: "name",
      render: (_, record) => (
        <div>
          <strong>{record.name}</strong>
          <Paragraph type="secondary" ellipsis={{ rows: 2 }} style={{ marginBottom: 0 }}>
            {record.description || "Chua co mo ta"}
          </Paragraph>
        </div>
      ),
    },
    {
      title: "Danh muc",
      dataIndex: "categoryName",
      key: "categoryName",
      width: 160,
      render: (categoryName) => <Tag color="blue">{categoryName}</Tag>,
    },
    {
      title: "Gia tien",
      dataIndex: "price",
      key: "price",
      width: 180,
      render: (price) => `${currencyFormatter.format(price ?? 0)} VND`,
    },
    {
      title: "Hanh dong",
      key: "actions",
      width: 160,
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" onClick={() => handleEditClick(record)}>
            Sua
          </Button>
          <Popconfirm
            title="Xoa san pham"
            description="Ban co chac chan muon xoa san pham nay?"
            okText="Xoa"
            cancelText="Huy"
            onConfirm={() => handleDelete(record.id)}
          >
            <Button danger type="link">
              Xoa
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div className="page-section-header">
        <div>
          <Title level={3} style={{ marginBottom: 4 }}>
            Quan ly san pham
          </Title>
          <Paragraph type="secondary" style={{ marginBottom: 0 }}>
            Theo doi danh sach san pham va cap nhat thong tin tu mot noi duy nhat.
          </Paragraph>
        </div>

        <Button type="primary" icon={<PlusOutlined />} onClick={handleCreateClick}>
          Them san pham
        </Button>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        dataSource={products}
        columns={columns}
        pagination={{ pageSize: 6, showSizeChanger: false }}
      />

      <ProductFormModal
        open={modalOpen}
        categories={categories}
        editingProduct={editingProduct}
        loading={saving}
        onCancel={closeModal}
        onSubmit={handleSave}
      />
    </div>
  );
};

export default ProductList;
