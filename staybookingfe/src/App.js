import './App.css';
import React from 'react';
import { UserOutlined } from "@ant-design/icons";
import { Button, Dropdown, Layout, Menu } from "antd";
import LoginPage from './components/LoginPage';
import HostHomePage from './components/HostHomePage';
import GuestHomePage from './components/GuestHomePage';

// const Header = Layout.Header;
// const Content = Layout.Content;
const { Header, Content } = Layout;

class App extends React.Component {
  state = {
    authed: false,
    asHost: false,
  };

  // at the beginning when the web page just open, 
  // we need to check whether the user is already logged in or not
  componentDidMount() {
    const authToken = localStorage.getItem("authToken");
    const asHost = localStorage.getItem("asHost") === "true";
    this.setState({
      authed: authToken !== null,
      asHost,
    });
  };

  // excuted only when login successed
  handleLoginSuccess = (token, asHost) => {
    localStorage.setItem("authToken", token);
    localStorage.setItem("asHost", asHost);
    this.setState({
      authed: true,
      asHost,
    });
  };

  handleLogOut = () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("asHost");
    this.setState({
      authed: false,
    });
  };

  renderContent = () => {
    if(!this.state.authed) {
      return <LoginPage handleLoginSuccess={this.handleLoginSuccess} />;
    }

    if(this.state.asHost) {
      return <HostHomePage />;
    }

    return <GuestHomePage />;
  }

  userMenu = (
    <Menu>
      <Menu.Item key="logout" onClick={this.handleLogOut}>
        Log Out
      </Menu.Item>
    </Menu>
  )

  render() {
    return (
      // vh: viewport height, define height so that the component height is not depends on content
      <Layout style={{height: "100vh"}}>
        <Header style={{display: "flex", justifyContent: "space-between"}}>
          <div style={{fontSize: 16, fontWeight: 600, color: "white"}}>
            Stays Booking
          </div>
            {
              this.state.authed && (
              <div>
                <Dropdown trigger="click" overlay={this.userMenu}>
                  <Button icon={<UserOutlined/>} shape="circle" />
                </Dropdown>
              </div>
            )}
        </Header>
        <Content style={{height: "calc(100% - 64px)", margin: 20, overflow: "auto"}}>
          {this.renderContent()}
        </Content>
      </Layout>
    )
  }
}

export default App;
