import {
  ADD_CONTENT_ITEM,
  EDIT_CONTENT_ITEM,
  REMOVE_CONTENT_ITEM,
  RECEIVE_CONTENT_ITEM,

  UPLOAD_CONTENT_ITEM,
  UPLOAD_CONTENT_ITEM_SUCCESS,
  UPLOAD_CONTENT_ITEM_FAILURE,
  UPLOAD_CONTENT_ITEM_PROGRESS,

  LOAD_CONTENT_ITEMS_SUCCESS,
  LOAD_CONTENT_ITEMS_FAILURE,
  LOAD_CONTENT_ITEMS_REQUEST,
} from '../state/ContentAction'

function ContentItemReducer(state = [], action) {
  // console.log('ContentItemReducer called: ' + JSON.stringify(state) + ' action: ' + JSON.stringify(action))

  switch (action.type) {

        case ADD_CONTENT_ITEM:{
         // ('LOGIN_USER received action:' + action.type + ' username: ' + action.username + ' password: ' + action.password)
          return{
              ...state,
              fetching:true,
            }
            break;
        }

        case UPLOAD_CONTENT_ITEM:{
          return{
            ...state,
            ...action.payload,
            fetching:false,
          }
          break;
        }

        /*case UPLOAD_CONTENT_ITEM_PROGRESS:{
          return{
            // ...state,
            ...action.payload
          }
          break;
        }*/

      case RECEIVE_CONTENT_ITEM:{
          // alert('RECEIVE_CONTENT_ITEM received action:' + action.type + ' content: ' + JSON.stringify(action.payload))
        return{
          ...state,
          ...action.payload,
          fetching:false,
        }
        break;
      }

      case UPLOAD_CONTENT_ITEM_FAILURE:{
        alert('UPLOAD_CONTENT_ITEM_FAILURE received action:' + action.type)
        return [
          {
            ...state,
            fetching:false,
            error:action.payload,
          }
        ];
        break;
      }

      case EDIT_CONTENT_ITEM:{
        return state.map((ContentItem, index) => {
          if (index === action.index) {
            return Object.assign({}, ContentItem, {
              completed: true
            })
          }
          return ContentItem
        })
        break;
      }

      case REMOVE_CONTENT_ITEM:{
        return state.map((ContentItem, index) => {
          if (index === action.index) {
            return Object.assign({}, ContentItem, {
              completed: true
            })
          }
          return ContentItem
        })
        break;
      }

    default:
      return state
  }
}

export default ContentItemReducer
