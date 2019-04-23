import React from 'react';

const Footer = () => {
    return (
        <div className="footer">
            <div className="container-fluid container-fluid-max-lg">
                <div className="row">
                    <div className="col-lg text-center text-lg-left mb-4 mb-lg-0">
                        <img className="logo" alt="starter logo" src="/logos/starter_logo_vertical_color@x2.png" />

                        <div className="clearfix d-lg-none"></div>

                        Built with love in San Francisco by <a href="http://www.starter.io" className="font-weight-bold"  target="_blank" rel="noopener noreferrer">Starter Inc.</a>
                    </div>

                    <div className="col-lg text-center text-lg-right">
                        <div>Powered by <a href="http://ignite.starter.io/" className="font-weight-bold"  target="_blank" rel="noopener noreferrer">Starter StackGen</a></div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Footer;
