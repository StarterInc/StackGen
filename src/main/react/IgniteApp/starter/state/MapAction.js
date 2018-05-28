export const DROP_PIN = 'DROP_PIN'

/**
  Basic Action Creator Methods
*/
export function dropPin(coords){ // fire event
  return {
    type: DROP_PIN,
    payload:coords
  }
}
