import Login from '../components/login';
import Logout from '../components/logout';
import Denied from "../components/denied";
import Index from "../components/index";

export const items =[
        {id:"/",to:"/",name:"index",component:Index,auth:false},
        {id:"login", to:"/login", name:"Login", component:Login,auth:false},
        {id:"logout", to:"/logout", name:"Logout", component:Logout,auth:true},
        {id:"denied", to:"/denied", name:"Denied", component:Denied,render:false}
    ];