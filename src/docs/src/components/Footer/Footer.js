import React from 'react';

const Footer = () => {
    return (
        <div className="footer">
            <div className="container-fluid container-fluid-max-lg">
                <div className="row">
                    <div className="col-lg text-center text-sm-left mb-4 mb-lg-0">

                    </div>
                    <div className="col-lg text-center text-sm-right">
                        <div>
<br/>Powered by <a href="http://docs.stackgen.io/" target="_blank" rel="noopener noreferrer"><span className="title align-middle">{process.env.PROJECT_NAME}</span></a></div>
                        Built with ❤️ in San Francisco by <a href="http://www.starter.io" className="font-weight-bold"  target="_blank" rel="noopener noreferrer">
                        <img className="logo" alt="starter logo" src="/logos/starter_logo_vertical_color@x2.png" />
                        </a>

                    </div>
                </div>
            </div>
        </div>
    );
}

export default Footer;
