/**
 * Csat Data Table
 * 
 * 
 * 
 * 
 * 
 *
 *
var Csat:{
		id: '',
		userId: '',
		contentId: '',
		comments: '',
		rating: '',
}
*/
import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { Card, Table, Row, Col, Button } from 'react-bootstrap';
import { listCsats, removeCsat } from '../../actions/Csats'

import Csat from './Csat';
import CsatHeader from './CsatHeader';

const CsatTable = (props) => (

    <Card style={{ width: '100%' }}>
    <Card.Header>
        <Row>
            <Col>
                Csat List
            </Col>
            <Col style={{float:"right"}}>
                <Link to={`/Csat/add/`}> <b>add</b></Link> | <Button onClick={ () => { props.dispatch(listCsats()); }}>load</Button>
            </Col>    
        </Row>
    </Card.Header>

        <Card.Body>
        <Table striped hover responsive size="lg">
            <CsatHeader/>
            <tbody>
            {typeof(props.Csats.map) !== 'undefined' ? props.Csats.map(csat => {
            	const {id} = csat;
                return (
                    <tr key={csat.id} id={csat.id}>
                        <Csat {...csat} />
                        <td>
                        <Button onClick={ () => { props.dispatch(removeCsat( { id } )); }}>
                        delete
                        </Button>
                        </td>
                    	<td><Link to={`/Csat/edit/${id}`}><b>edit</b></Link></td>
                    </tr>
                );
            }): <tr><td>no results</td></tr> }
            </tbody>
            </Table>
        </Card.Body>
    </Card>

);

const mapStateToProps = (state) => {
    return {
        Csats: state.Csats 
    };
}

export default connect(mapStateToProps)(CsatTable);
