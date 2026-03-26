import React, { useEffect, useState } from "react";
import {
  Table,
  Image,
  Tag,
  message,
  Typography,
  Button,
  Modal,
  Form,
  Input,
  InputNumber,
  Select,
  Popconfirm,
} from "antd";
import { PlusOutlined } from "@ant-design/icons";
import api from "../api/api";

const { Title } = Typography;
const { Option } = Select;

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form] = Form.useForm();
  const [editingProduct, setEditingProduct] = useState(null);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [prodRes, catRes] = await Promise.all([
        api.get("/products"),
        api.get("/categories"),
      ]);
      setProducts(prodRes.data);
      setCategories(catRes.data);
    } catch (error) {
      message.error("Lỗi khi tải dữ liệu!");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = async (id) => {
    try {
      await api.delete(`/products/${id}`);
      message.success("Đã xóa sản phẩm thành công!");
      fetchData();
    } catch (error) {
      message.error("Lỗi khi xóa sản phẩm!");
    }
  };

  const showEditModal = (product) => {
    setEditingProduct(product);
    const category = categories.find((c) => c.name === product.categoryName);

    form.setFieldsValue({
      ...product,
      categoryId: category?.id,
    });
    setIsModalOpen(true);
  };

  // Hàm này là "trái tim" của CRUD: xử lý cả Thêm và Sửa
  const handleSave = async (values) => {
    try {
      if (editingProduct) {
        await api.put(`/products/${editingProduct.id}`, values);
        message.success("Cập nhật sản phẩm thành công!");
      } else {
        await api.post("/products", values);
        message.success("Thêm sản phẩm thành công!");
      }
      setIsModalOpen(false);
      setEditingProduct(null);
      form.resetFields();
      fetchData();
    } catch (error) {
      message.error("Đã có lỗi xảy ra!");
    }
  };

  const columns = [
    {
      title: "Hình ảnh",
      dataIndex: "imageUrl",
      key: "imageUrl",
      render: (text) => (
        <Image
          src={text}
          width={50}
          fallback="https://via.placeholder.com/50"
        />
      ),
    },
    {
      title: "Tên sản phẩm",
      dataIndex: "name",
      key: "name",
      render: (text) => <b>{text}</b>,
    },
    {
      title: "Danh mục",
      dataIndex: "categoryName",
      key: "categoryName",
      render: (cat) => <Tag color="blue">{cat}</Tag>,
    },
    {
      title: "Giá tiền",
      dataIndex: "price",
      key: "price",
      render: (p) => `${p?.toLocaleString()} VNĐ`,
    },
    {
      title: "Hành động",
      key: "action",
      render: (_, record) => (
        <div style={{ display: "flex", gap: "10px" }}>
          <Button type="link" onClick={() => showEditModal(record)}>
            Sửa
          </Button>
          <Popconfirm
            title="Xóa sản phẩm"
            onConfirm={() => handleDelete(record.id)}
            okText="Xóa"
            cancelText="Hủy"
          >
            <Button type="link" danger>
              Xóa
            </Button>
          </Popconfirm>
        </div>
      ),
    },
  ];

  return (
    <div>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          marginBottom: 20,
        }}
      >
        <Title level={3}>Quản lý Sản phẩm</Title>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => {
            setEditingProduct(null); // Đảm bảo reset trạng thái edit khi thêm mới
            form.resetFields();
            setIsModalOpen(true);
          }}
        >
          Thêm sản phẩm
        </Button>
      </div>

      <Table
        columns={columns}
        dataSource={products}
        rowKey="id"
        loading={loading}
      />

      <Modal
        title={editingProduct ? "Chỉnh sửa sản phẩm" : "Thêm sản phẩm mới"}
        open={isModalOpen}
        onCancel={() => {
          setIsModalOpen(false);
          setEditingProduct(null);
          form.resetFields();
        }}
        onOk={() => form.submit()}
        okText="Lưu"
        cancelText="Hủy"
      >
        {/* QUAN TRỌNG: Đổi onFinish thành handleSave */}
        <Form form={form} layout="vertical" onFinish={handleSave}>
          <Form.Item
            name="name"
            label="Tên sản phẩm"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="categoryId"
            label="Danh mục"
            rules={[{ required: true }]}
          >
            <Select placeholder="Chọn danh mục">
              {Array.isArray(categories) &&
                categories.map((cat) => (
                  <Option key={cat.id} value={cat.id}>
                    {cat.name}
                  </Option>
                ))}
            </Select>
          </Form.Item>
          <Form.Item name="price" label="Giá tiền" rules={[{ required: true }]}>
            <InputNumber
              style={{ width: "100%" }}
              formatter={(value) =>
                `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
              }
            />
          </Form.Item>
          <Form.Item name="imageUrl" label="Link hình ảnh">
            <Input placeholder="https://..." />
          </Form.Item>
          <Form.Item name="description" label="Mô tả">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ProductList;
