package ak.enchantchanger.capability;

/**
 * エンチャント交換MODでプレイヤー側に保持するステータスのインターフェース
 * Created by A.K. on 2017/04/18.
 */
public interface IPlayerStatusHandler {
    /**
     * APオーブ取得時のクールタイム取得
     *
     * @return 次のオーブ取得までの時間
     */
    long getApCoolingTime();

    /**
     * APオーブ取得時のクールタイム設定
     *
     * @param time 次のオーブ取得までの時間
     */
    void setApCoolingTime(long time);

    /**
     * ソルジャーの証を使用したかどうかの取得<br />
     * マテリア設定画面はtrueのときのみ開ける
     *
     * @return 設定値
     */
    boolean getSoldierMode();

    /**
     * ソルジャーの証を使用したかどうかの設定
     *
     * @param mode 設定値
     */
    void setSoldierMode(boolean mode);

    /**
     * レビテト使用時かどうかの取得
     *
     * @return true:レビテト状態
     */
    boolean isLevitating();

    /**
     * レビテト使用直どうかの設定
     *
     * @param isLevitating レビテト状態
     */
    void setLevitating(boolean isLevitating);

    /**
     * リミットゲージへの値の追加
     *
     * @param value 追加値
     */
    void addLimitGaugeValue(int value);

    /**
     * リミットゲージの値の取得
     *
     * @return リミットゲージ値
     */
    int getLimitGaugeValue();

    /**
     * リミットゲージの値の設定
     *
     * @param value リミットゲージ値
     */
    void setLimitGaugeValue(int value);

    /**
     * リミットブレイクできるかどうか<br />
     * 基本的にlimitGaugeValueから判定する。
     *
     * @return true:リミットブレイク可能
     */
    boolean canLimitBreak();

    /**
     * ソルジャーとして働き始めた時間の取得<br />
     * MCEとの連携で使用
     *
     * @return ソルジャーとして働き始めた時間
     */
    long getSoldierWorkStartTime();

    /**
     * ソルジャーとして働き始めた時間の設定
     *
     * @param soldierWorkStartTime ソルジャーとして働き始めた時間
     */
    void setSoldierWorkStartTime(long soldierWorkStartTime);

    /**
     * 追加武器によるモブ殺害数の取得
     *
     * @return 追加武器によるモブ殺害数
     */
    int getMobKillCount();

    /**
     * 追加武器によるモブ殺害数の設定
     *
     * @param mobKillCount 追加武器によるモブ殺害数
     */
    void setMobKillCount(int mobKillCount);

    /**
     * リミットブレイク時間<br />
     * 0でリミットブレイク終了
     *
     * @return リミットブレイク時間
     */
    int getLimitBreakCount();

    /**
     * リミットブレイク時間の設定<br />
     * リミットブレイク開始時に利用
     *
     * @param limitBreakCount リミットブレイク時間
     */
    void setLimitBreakCount(int limitBreakCount);

    /**
     * リミットブレイク時間を減らす処理
     */
    void decreaseLimitBreakCount();

    /**
     * リミットブレイク中かどうか
     *
     * @return true:リミットブレイク中
     */
    boolean isLimitBreaking();

    /**
     * リミットブレイクのIdを取得
     *
     * @return limitBreakId
     */
    byte getLimitBreakId();

    /**
     * リミットブレイクのIdを設定
     *
     * @param limitBreakId limitBreakId
     */
    void setLimitBreakId(byte limitBreakId);

    /**
     * GreatGospel中かどうか
     *
     * @return true:GreatGospel中
     */
    boolean isGgMode();

    /**
     * GreatGospel状態の設定
     *
     * @param ggMode ggMode
     */
    void setGgMode(boolean ggMode);
}
