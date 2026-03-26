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

  const handleCreate = async (values) => {
    try {
      await api.post("/products", values);
      message.success("Thêm sản phẩm thành công!");
      setIsModalOpen(false);
      form.resetFields();
      fetchData(); // Load lại bảng
    } catch (error) {
      message.error("Lỗi khi thêm sản phẩm!");
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
          onClick={() => setIsModalOpen(true)}
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
        title="Thêm sản phẩm mới"
        open={isModalOpen}
        onCancel={() => setIsModalOpen(false)}
        onOk={() => form.submit()}
        okText="Lưu"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical" onFinish={handleCreate}>
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
              {/* Thêm đoạn Array.isArray() để kiểm tra chắc chắn là mảng trước khi map */}
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
