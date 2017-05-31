/**
 * Function that logout user. It clears session and redirect user to login page.
 * 
 * @returns
 */
function logout() {
	sessionStorage.clear();
	window.location.href = "/";
}