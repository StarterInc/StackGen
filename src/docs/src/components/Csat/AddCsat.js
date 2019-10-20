/*
 * Add Form wrapper
 *
 * 
 * 
 * 
 * 
 *
 */
import React from 'react';
import { connect } from 'react-redux';
import { Card } from 'react-bootstrap';

import CsatForm from './CsatForm';
import { addCsat } from '../../actions/Csats';

const AddCsat = (props) => (
    <Card>
        <h3>Enter New Csat</h3>
        <CsatForm
            onSubmitCsat={(csat) => {
                props.dispatch(addCsat(csat));
                props.history.push('/');
            }}
        />
    </Card>
);

function mapStateToProps(state) {
    return { Csat: state.Csat }
}
export default connect(mapStateToProps)(AddCsat);
