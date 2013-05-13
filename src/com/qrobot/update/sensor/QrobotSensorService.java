/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\QroService\\src\\com\\qrobot\\service\\QrobotSensorService.aidl
 */
package com.qrobot.update.sensor;

public interface QrobotSensorService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.qrobot.update.sensor.QrobotSensorService
{
private static final java.lang.String DESCRIPTOR = "com.qrobot.service.QrobotSensorService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.qrobot.service.QrobotSensorService interface,
 * generating a proxy if needed.
 */
public static com.qrobot.update.sensor.QrobotSensorService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.qrobot.update.sensor.QrobotSensorService))) {
return ((com.qrobot.update.sensor.QrobotSensorService)iin);
}
return new com.qrobot.update.sensor.QrobotSensorService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setRTCType:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setRTCType(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_irVolumeUp:
{
data.enforceInterface(DESCRIPTOR);
this.irVolumeUp();
reply.writeNoException();
return true;
}
case TRANSACTION_irVolumeDown:
{
data.enforceInterface(DESCRIPTOR);
this.irVolumeDown();
reply.writeNoException();
return true;
}
case TRANSACTION_irChanelUp:
{
data.enforceInterface(DESCRIPTOR);
this.irChanelUp();
reply.writeNoException();
return true;
}
case TRANSACTION_irChanelDown:
{
data.enforceInterface(DESCRIPTOR);
this.irChanelDown();
reply.writeNoException();
return true;
}
case TRANSACTION_irShutdown:
{
data.enforceInterface(DESCRIPTOR);
this.irShutdown();
reply.writeNoException();
return true;
}
case TRANSACTION_irSetup:
{
data.enforceInterface(DESCRIPTOR);
this.irSetup();
reply.writeNoException();
return true;
}
case TRANSACTION_irNumKey:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.irNumKey(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_tempRead:
{
data.enforceInterface(DESCRIPTOR);
float _result = this.tempRead();
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_lktRegistration:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
int _arg1;
_arg1 = data.readInt();
this.lktRegistration(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_lktCertification:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
byte[] _result = this.lktCertification(_arg0);
reply.writeNoException();
reply.writeByteArray(_result);
return true;
}
case TRANSACTION_lktID:
{
data.enforceInterface(DESCRIPTOR);
byte[] _result = this.lktID();
reply.writeNoException();
reply.writeByteArray(_result);
return true;
}
case TRANSACTION_lktEncryption:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
byte _arg1;
_arg1 = data.readByte();
byte[] _result = this.lktEncryption(_arg0, _arg1);
reply.writeNoException();
reply.writeByteArray(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.qrobot.update.sensor.QrobotSensorService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
 * ����ң����ң�ض��������
 * @param typeCode 1,tv;2,dvd
 */
@Override public void setRTCType(int typeCode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(typeCode);
mRemote.transact(Stub.TRANSACTION_setRTCType, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * ���������������
 */
@Override public void irVolumeUp() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_irVolumeUp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * �������������С
 */
@Override public void irVolumeDown() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_irVolumeDown, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * �������Ƶ������
 */
@Override public void irChanelUp() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_irChanelUp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * �������Ƶ����С
 */
@Override public void irChanelDown() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_irChanelDown, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * ������ƹػ�
 */
@Override public void irShutdown() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_irShutdown, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * �����������
 */
@Override public void irSetup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_irSetup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * ����������ְ���
 * @param num 0-9
 */
@Override public void irNumKey(int num) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(num);
mRemote.transact(Stub.TRANSACTION_irNumKey, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * ��ȡ�¶�
 * @return �¶�ֵ
 */
@Override public float tempRead() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_tempRead, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
 * ������оƬָ����ַд��ע���� 
 * @param id_code 18���ֽڵ�ע����
 * @param address ����ID 1;
			<p> ������ID 2;
			<p> ��֤��ǰ18λ��3;
			<p> ��֤���18Ϊ��4;
			Ĭ��Ϊ������id
 */
@Override public void lktRegistration(byte[] id_code, int address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(id_code);
_data.writeInt(address);
mRemote.transact(Stub.TRANSACTION_lktRegistration, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
 * �Ӽ���оƬָ����ַ����ע���� 
 * @param address ����ID 1;
			<p> ������ID 2;
			<p> ��֤��ǰ18λ��3;
			<p> ��֤���18Ϊ��4;
			Ĭ��Ϊ������id
 * @return ע����
 */
@Override public byte[] lktCertification(int address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(address);
mRemote.transact(Stub.TRANSACTION_lktCertification, _data, _reply, 0);
_reply.readException();
_result = _reply.createByteArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
 * ��ȡ����оƬID��
 * @return ID��
 */
@Override public byte[] lktID() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_lktID, _data, _reply, 0);
_reply.readException();
_result = _reply.createByteArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
 * ������֤�����������ܺ�͵�10λIDһͬ���� 
 * @param  �����random[], ���ܷ�ʽalgorithm_type
 * @return ע����
 */
@Override public byte[] lktEncryption(byte[] random, byte algorithm_type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(random);
_data.writeByte(algorithm_type);
mRemote.transact(Stub.TRANSACTION_lktEncryption, _data, _reply, 0);
_reply.readException();
_result = _reply.createByteArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setRTCType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_irVolumeUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_irVolumeDown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_irChanelUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_irChanelDown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_irShutdown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_irSetup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_irNumKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_tempRead = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_lktRegistration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_lktCertification = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_lktID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_lktEncryption = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
}
/**
 * ����ң����ң�ض��������
 * @param typeCode 1,tv;2,dvd
 */
public void setRTCType(int typeCode) throws android.os.RemoteException;
/**
 * ���������������
 */
public void irVolumeUp() throws android.os.RemoteException;
/**
 * �������������С
 */
public void irVolumeDown() throws android.os.RemoteException;
/**
 * �������Ƶ������
 */
public void irChanelUp() throws android.os.RemoteException;
/**
 * �������Ƶ����С
 */
public void irChanelDown() throws android.os.RemoteException;
/**
 * ������ƹػ�
 */
public void irShutdown() throws android.os.RemoteException;
/**
 * �����������
 */
public void irSetup() throws android.os.RemoteException;
/**
 * ����������ְ���
 * @param num 0-9
 */
public void irNumKey(int num) throws android.os.RemoteException;
/**
 * ��ȡ�¶�
 * @return �¶�ֵ
 */
public float tempRead() throws android.os.RemoteException;
/**
 * ������оƬָ����ַд��ע���� 
 * @param id_code 18���ֽڵ�ע����
 * @param address ����ID 1;
			<p> ������ID 2;
			<p> ��֤��ǰ18λ��3;
			<p> ��֤���18Ϊ��4;
			Ĭ��Ϊ������id
 */
public void lktRegistration(byte[] id_code, int address) throws android.os.RemoteException;
/**
 * �Ӽ���оƬָ����ַ����ע���� 
 * @param address ����ID 1;
			<p> ������ID 2;
			<p> ��֤��ǰ18λ��3;
			<p> ��֤���18Ϊ��4;
			Ĭ��Ϊ������id
 * @return ע����
 */
public byte[] lktCertification(int address) throws android.os.RemoteException;
/**
 * ��ȡ����оƬID��
 * @return ID��
 */
public byte[] lktID() throws android.os.RemoteException;
/**
 * ������֤�����������ܺ�͵�10λIDһͬ���� 
 * @param  �����random[], ���ܷ�ʽalgorithm_type
 * @return ע����
 */
public byte[] lktEncryption(byte[] random, byte algorithm_type) throws android.os.RemoteException;
}