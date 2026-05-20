"""
jwt_bearer.py
-------------
FastAPI dependency for JWT authentication.

Validates the Bearer token sent in the Authorization header against
the secret key shared with UserService. Extracts and exposes the
decoded token payload (id, email, role) for use in route handlers.
"""

import jwt
from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials

SECRET_KEY = "skdhfasdhfkhsdfjhsdkjhfjsdhfk"
ALGORITHM = "HS256"

bearer_scheme = HTTPBearer()


def verify_token(credentials: HTTPAuthorizationCredentials = Depends(bearer_scheme)) -> dict:
    """
    Dependency that validates the JWT token from the Authorization header.
    Returns the decoded payload if valid, raises 401 otherwise.
    """
    token = credentials.credentials
    try:
        payload = jwt.decode(
            token,
            SECRET_KEY,
            algorithms=[ALGORITHM],
            options={"verify_iss": True},
            issuer="Medly",
        )
        return payload
    except jwt.ExpiredSignatureError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Token has expired",
        )
    except jwt.InvalidTokenError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid token",
        )