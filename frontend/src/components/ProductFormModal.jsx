import { Form, Input, InputNumber, Modal, Select } from "antd";
import { useEffect } from "react";

const ProductFormModal = ({
  open,
  categories,
  editingProduct,
  loading,
  onCancel,
  onSubmit,
}) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (!open) {
      form.resetFields();
      return;
    }

    if (editingProduct) {
      form.setFieldsValue({
        name: editingProduct.name,
        price: editingProduct.price,
        description: editingProduct.description,
        imageUrl: editingProduct.imageUrl,
        categoryId: editingProduct.categoryId,
      });
      return;
    }

    form.resetFields();
  }, [editingProduct, form, open]);

  const handleCancel = () => {
    form.resetFields();
    onCancel();
  };

  return (
    <Modal
      title={editingProduct ? "Chinh sua san pham" : "Them san pham"}
      open={open}
      confirmLoading={loading}
      okText="Luu"
      cancelText="Huy"
      destroyOnClose
      onCancel={handleCancel}
      onOk={() => form.submit()}
    >
      <Form form={form} layout="vertical" onFinish={onSubmit}>
        <Form.Item
          name="name"
          label="Ten san pham"
          rules={[{ required: true, message: "Vui long nhap ten san pham." }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          name="categoryId"
          label="Danh muc"
          rules={[{ required: true, message: "Vui long chon danh muc." }]}
        >
          <Select
            placeholder="Chon danh muc"
            options={categories.map((category) => ({
              label: category.name,
              value: category.id,
            }))}
          />
        </Form.Item>

        <Form.Item
          name="price"
          label="Gia tien"
          rules={[{ required: true, message: "Vui long nhap gia tien." }]}
        >
          <InputNumber
            min={0}
            style={{ width: "100%" }}
            formatter={(value) =>
              `${value ?? ""}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
            }
          />
        </Form.Item>

        <Form.Item name="imageUrl" label="Link hinh anh">
          <Input placeholder="https://..." />
        </Form.Item>

        <Form.Item name="description" label="Mo ta">
          <Input.TextArea rows={4} />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default ProductFormModal;
