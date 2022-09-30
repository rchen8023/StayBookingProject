import React from "react";
import { Button, Form, Input, message, InputNumber, Layout } from "antd";
import { uploadStay } from "../utils";

const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
}

class UploadStay extends React.Component {
  fileInputRef = React.createRef();

  state = {
    loading: false,
  }

  handleSubmit = async (data) => {
    const formData = new FormData()
    const { files } = this.fileInputRef.current;

    if(files.length > 5) {
      message.error("You can at most upload 5 pictures.")
      return;
    }

    for(let i = 0; i < files.length; i++) {
      formData.append("images", files[i]);
    }

    formData.append("name", data.name);
    formData.append("address", data.address);
    formData.append("description", data.description);
    formData.append("guest_number", data.guest_number);

    this.setState({
      loading: true,
    })

    try{
      await uploadStay(formData);
      message.success("upload successfully");
    } catch (error) {
      message.error(error.message);
    } finally {
      this.setState({
        loading: false,
      });
    }
  }

  render() {
    return (
      <Form
        name="basic"
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
        onFinish={this.handleSubmit}
        style={{maxWidth: 1000, margin: "auto"}}
      >
        <Form.Item label="Name" name="name" rules={[{ required: true }]} >
          <Input />
        </Form.Item>
  
        <Form.Item label="Address" name="address" rules={[{ required: true}]} >
          <Input />
        </Form.Item>

        <Form.Item name="description" label="Description" rules={[{ required: true }]}>
          <Input.TextArea autoSize={{ minRows: 2, maxRows: 6 }} />
        </Form.Item>

        <Form.Item name="guest_number" label="Guest Number" rules={[{ required: true, type: "number", min: 1 }]} >
          <InputNumber />
        </Form.Item>

        <Form.Item name="picture" label="Picture" rules={[{ required: true }]}>
          <input
            type="file"
            accept="image/png, image/jpeg"
            ref={this.fileInputRef}
            multiple={true}
          />
        </Form.Item>

        <Form.Item wrapperCol={{ ...layout.wrapperCol, offset: 8}}>
          <Button type="primary" htmlType="submit" loading={this.state.loading}>
            Submit
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

export default UploadStay;