(this["webpackJsonpreact-lab-cs32"]=this["webpackJsonpreact-lab-cs32"]||[]).push([[0],{21:function(e,t,n){},4:function(e,t,n){},45:function(e,t,n){"use strict";n.r(t);var a=n(1),c=n.n(a),o=n(14),r=n.n(o),s=(n(21),n.p+"static/media/logo.6ce24c58.svg"),l=(n(4),n(3)),i=n(0);var j,u,d,h,b=function(e){return Object(i.jsxs)("div",{className:"TextBox",children:[e.label,Object(i.jsx)("input",{type:"text",label:e.label,onChange:function(t){return e.onChange(t.target.value)}})]})},g=n(15),p=(n(26),n(16)),O=n.n(p);var x=function(e){var t=600,n=600,c=Object(a.useRef)();return Object(a.useEffect)((function(){d=c.current;var a=function(e){var t=e.backingStorePixelRatio||e.webkitBackingStorePixelRatio||e.mozBackingStorePixelRatio||e.msBackingStorePixelRatio||e.oBackingStorePixelRatio||e.backingStorePixelRatio||1;return(window.devicePixelRatio||1)/t}(h=d.getContext("2d")),o=getComputedStyle(d).getPropertyValue("width").slice(0,-2),r=getComputedStyle(d).getPropertyValue("height").slice(0,-2);d.width=o*a,d.height=r*a,d.style.width="".concat(o,"px"),d.style.height="".concat(r,"px"),h.clearRect(0,0,d.width,d.height),h.lineWidth=1,8e3,41.823876,-71.395963,j=41.748875999999996,u=-71.470963;var s,l,i,b,g,p,O=e.routetorender,x=[];null!=O&&(x=(O+"").split(";")),console.log("low"+x.length);for(var f=0;f<x.length;f++)l=(s=x[f].split(","))[0],i=parseFloat(s[1]),b=parseFloat(s[2]),g=parseFloat(s[3]),p=parseFloat(s[4]),500===f&&console.log(n*(b-u)/.07500000000000284),h.beginPath(),h.moveTo(t*(i-j)/.07500000000000284-300,n*(b-u)/.07500000000000284-300),h.lineTo(t*(g-j)/.07500000000000284-300,n*(p-u)/.07500000000000284-300),h.strokeStyle="red",""!==l&&"unclassified"!==l||(h.strokeStyle="blue"),h.stroke();console.log("done")})),Object(i.jsx)("div",{className:"Canvas",children:Object(i.jsx)("canvas",{ref:c,style:{width:t,height:n},routetorender:e.routetorender,onChange:function(t){return e.onChange(t.target.value)}})})};var f=function(){var e=Object(a.useState)(0),t=Object(l.a)(e,2),n=t[0],c=t[1],o=Object(a.useState)(0),r=Object(l.a)(o,2),s=r[0],j=r[1],u=Object(a.useState)(0),d=Object(l.a)(u,2),h=d[0],p=d[1],f=Object(a.useState)(0),v=Object(l.a)(f,2),S=v[0],m=v[1],C=Object(a.useState)(""),k=Object(l.a)(C,2),y=(k[0],k[1]),w=Object(a.useState)([]),P=Object(l.a)(w,2),R=P[0],B=P[1];return Object(i.jsxs)("div",{className:"Route",children:[Object(i.jsx)("header",{className:"Route-header",children:Object(i.jsx)("title",{children:"This is a title"})}),Object(i.jsx)("h1",{children:n}),Object(i.jsx)(b,{label:"Start Latitude",onChange:c}),Object(i.jsx)(b,{label:"Start Longitude",onChange:j}),Object(i.jsx)(b,{label:"End Latitude",onChange:p}),Object(i.jsx)(b,{label:"End Longitude",onChange:m}),Object(i.jsx)(g.AwesomeButton,{type:"primary",onPress:function(){!function(){var e={srclat:n,srclong:s,destlat:h,destlong:S};O.a.post("http://localhost:4567/route",e,{headers:{"Content-Type":"application/json","Access-Control-Allow-Origin":"*"}}).then((function(e){console.log(e.data),B(e.data.route)})).catch((function(e){console.log(e)}))}()},children:"Button"}),Object(i.jsx)("p",{children:R[2]}),Object(i.jsx)(x,{routetorender:R[3],onChange:y})]})};var v=function(){return Object(i.jsx)("div",{className:"App",children:Object(i.jsxs)("header",{className:"App-header",children:[Object(i.jsx)(f,{}),Object(i.jsx)("img",{src:s,className:"App-logo",alt:"logo"}),Object(i.jsxs)("p",{children:["Edit ",Object(i.jsx)("code",{children:"src/App.js"})," and save to reload."]}),Object(i.jsx)("a",{className:"App-link",href:"https://reactjs.org",target:"_blank",rel:"noopener noreferrer",children:"Learn React"})]})})},S=function(e){e&&e instanceof Function&&n.e(3).then(n.bind(null,46)).then((function(t){var n=t.getCLS,a=t.getFID,c=t.getFCP,o=t.getLCP,r=t.getTTFB;n(e),a(e),c(e),o(e),r(e)}))};r.a.render(Object(i.jsx)(c.a.StrictMode,{children:Object(i.jsx)(v,{})}),document.getElementById("root")),S()}},[[45,1,2]]]);
//# sourceMappingURL=main.bb2bea67.chunk.js.map