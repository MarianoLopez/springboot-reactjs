import React, { Component } from 'react';

export default class Login extends Component {
    componentWillMount(){

    }

    componentWillUnmount(){

    }

    onLogin = () => { this.props.history.push('/') };

    onLoginFail = (err) => { document.getElementById("error_span").innerHTML = err.data.message };


    submit=(e)=>{
        e.preventDefault();


    };

    render() {
        return (
            <div className="row">
                <div title="Login" className="col s12 m10 l6 offset-m1 offset-l3">
                    <form onSubmit={this.submit}>
                        <div className="row">
                            <div className="input-field col s6 ">
                                <label htmlFor="username" className="active">Username</label>
                                <input id="username" type="text" className="validate"/>
                            </div>
                            <div className="input-field col s6">
                                <label htmlFor="password" className="active">Password</label>
                                <input id="password" type="password" className="validate"/>
                            </div>
                        </div>
                        <div className="input-field col s12 row">
                            <button className="btn waves-effect waves-light" type="submit" name="action">Submit <i className="material-icons right">send</i></button>
                        </div>
                        <div className="row">
                            <span className="red-text text-darken-2" id="error_span"></span>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}