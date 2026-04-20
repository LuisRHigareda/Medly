"""
base.py
-------
Defines the SQLAlchemy declarative base class used by all ORM models
in this service. Every model class must inherit from Base so that
SQLAlchemy can track the table mappings.
"""

from sqlalchemy.orm import DeclarativeBase


class Base(DeclarativeBase):
    """Base class for all SQLAlchemy ORM models in MedicalRecordService."""
    pass
