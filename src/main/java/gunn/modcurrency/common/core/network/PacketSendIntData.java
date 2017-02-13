package gunn.modcurrency.common.core.network;

import gunn.modcurrency.api.ModTile;
import gunn.modcurrency.common.tiles.TileSeller;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Distributed with the Currency-Mod for Minecraft.
 * Copyright (C) 2016  Brady Gunn
 *
 * File Created on 2016-11-06.
 */
public class PacketSendIntData implements IMessage {
    private BlockPos blockPos;
    private int data, mode;

    public PacketSendIntData() {
    }

    public void setData(int data, BlockPos pos, int mode) {
        this.blockPos = pos;
        this.data = data;
        this.mode = mode;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        data = buf.readInt();
        mode = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
        buf.writeInt(data);
        buf.writeInt(mode);
    }

    public static class Handler implements IMessageHandler<PacketSendIntData, IMessage> {

        @Override
        public IMessage onMessage(final PacketSendIntData message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendIntData message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().playerEntity;
            World world = playerEntity.world;
            switch (message.mode) {
                case 0:     //BlockBuy set Lock [to server]
                    ModTile te0= (ModTile) world.getTileEntity(message.blockPos);
                    te0.setField(1, message.data);
                    break;
                case 1:     //Block Vendor set Cost [to server]
                    ModTile te1= (ModTile) world.getTileEntity(message.blockPos);
                    te1.setItemCost(message.data);
                    break;
                case 2:     //Block Vendor, updated cost [to Client]
                    ModTile te2= (ModTile) world.getTileEntity(message.blockPos);
                    te2.setItemCost(message.data);
                    break;
                case 3:     //Enable/Disable Creative Button [to server]
                    ModTile te3= (ModTile) world.getTileEntity(message.blockPos);
                    te3.setField(6, message.data);
                    break;
                case 4:     //Send Gear Tab State [to server]
                    ModTile te4= (ModTile) world.getTileEntity(message.blockPos);
                    te4.setField(8, message.data);
                    break;
                case 5:     //Update Client
                    ModTile te5= (ModTile) world.getTileEntity(message.blockPos);
                    te5.update(world,message.blockPos);
                    break;
                case 6:     //Block Seller set Amount [to server]
                    TileSeller te6= (TileSeller) world.getTileEntity(message.blockPos);
                    te6.setItemAmount(message.data);
                    break;
                case 7:     //BlockBuy set Fuzzy [to server]
                    ModTile te7= (ModTile) world.getTileEntity(message.blockPos);
                    te7.setField(11, message.data);
                    break;
            }
        }
    }
}
