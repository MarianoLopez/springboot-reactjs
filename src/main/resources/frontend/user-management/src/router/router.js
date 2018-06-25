import React, { Component } from 'react';
import { BrowserRouter, Route, Switch,Redirect} from "react-router-dom";
import {items} from './configuration'
import { WebsocketView } from 'react-websocket-view';



export default class Router extends Component {
    constructor(props){
        super(props);
        this.routes = items.map((item)=>{
            if(item.roles){
                return <PrivateRoute  key={item.id} exact path={item.to} component={item.component} roles={item.roles} redirect="/login"/>
            } else{
                return <Route  key={item.id} exact path={item.to} component={item.component}/>
            }
        });
    }
    render() {
        return (
            <BrowserRouter>
                <div>
                    <main>
                        <Switch>{this.routes}</Switch>
                    </main>
                </div>
            </BrowserRouter>
        );
    }
}


const PrivateRoute = ({ component: Component,redirect,roles, ...rest }) => (
    <Route
        {...rest}
        render={props =>{
                const view =  <Component {...props} />;
                const view_denied = <Redirect
                    to={{
                        pathname: "/denied",
                        state: { from: props.location }
                    }}
                />;
                if(false){//userService.isLoggedIn()
                    /*if(roles){
                        if(userService.auth && userService.auth.roles){
                            let hasAnyRole = false;
                            for(let i=0;i<roles.length;i++) {
                                if (userService.auth.roles.includes(roles[i])) {
                                    hasAnyRole = true;
                                    break;
                                }
                            }
                            return (hasAnyRole)? view: view_denied
                        }else{
                            return view_denied;
                        }
                    }else{
                        return view
                    }*/
                }else{
                    return <Redirect
                        to={{
                            pathname: redirect,
                            state: { from: props.location }
                        }}
                    />
                }
            }
        }
    />
);